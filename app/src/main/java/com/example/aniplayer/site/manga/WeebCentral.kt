package com.example.aniplayer.site.manga

import android.os.Parcelable
import android.util.Log
import com.example.aniplayer.R
import com.example.aniplayer.model.manga.Manga
import com.example.aniplayer.model.manga.MangaChapter
import com.example.aniplayer.model.manga.MangaListFilter
import com.example.aniplayer.model.manga.MangaListFilterCapabilities
import com.example.aniplayer.model.manga.MangaListFilterOptions
import com.example.aniplayer.model.manga.MangaPage
import com.example.aniplayer.model.manga.MangaTag
import com.example.aniplayer.model.parsers.ContentType.COMICS
import com.example.aniplayer.model.parsers.ContentType.MANGA
import com.example.aniplayer.model.parsers.ContentType.MANHUA
import com.example.aniplayer.model.parsers.ContentType.MANHWA
import com.example.aniplayer.model.parsers.SortOrder
import com.example.aniplayer.model.parsers.SortOrder.POPULARITY
import com.example.aniplayer.model.parsers.SortOrder.UPDATED
import com.example.aniplayer.model.parsers.State.ABANDONED
import com.example.aniplayer.model.parsers.State.FINISHED
import com.example.aniplayer.model.parsers.State.ONGOING
import com.example.aniplayer.model.parsers.State.PAUSED
import com.example.aniplayer.model.site.Source
import com.example.aniplayer.utils.parsers.attrAsAbsoluteUrl
import com.example.aniplayer.utils.parsers.attrAsAbsoluteUrlOrNull
import com.example.aniplayer.utils.parsers.mapChapters
import com.example.aniplayer.utils.parsers.mapToSet
import com.example.aniplayer.utils.parsers.nullIfEmpty
import com.example.aniplayer.utils.parsers.parseHtml
import com.example.aniplayer.utils.parsers.selectFirstOrThrow
import com.example.aniplayer.utils.parsers.tryParse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.EnumSet
import java.util.Locale

@Parcelize
internal class WeebCentral : MangaSite(), Parcelable {

    override var domain = "weebcentral.com"
    override var source: Source = Source(
        name = SOURCE_NAME, image = R.drawable.site_weebcentral
    )
    override var offset = OFFSET

    override val availableSortOrders: Set<SortOrder>
        get() = EnumSet.of(
            POPULARITY,
            UPDATED,
        )

    override val filterCapabilities: MangaListFilterCapabilities
        get() = MangaListFilterCapabilities(
            isMultipleTagsSupported = true,
            isSearchSupported = true,
            isSearchWithFiltersSupported = true,
        )

    suspend fun getFilterOptions(): MangaListFilterOptions {
        val document = webClient.httpGet("https://$domain/search").parseHtml()

        val tags =
            document.select("section[x-show=show_filter] div:contains(tags) ~ fieldset label").map {
                MangaTag(
                    title = it.selectFirstOrThrow(".label-text").text(),
                    key = it.selectFirstOrThrow("input[id$=value]").attr("value"),
                    source = source,
                )
            }.toSet()

        val states = EnumSet.of(
            ONGOING, FINISHED, ABANDONED, PAUSED,
        )

        val types = EnumSet.of(
            MANGA, MANHWA, MANHUA, COMICS,
        )

        return MangaListFilterOptions(
            availableTags = tags,
            availableStates = states,
            availableContentTypes = types,
        )
    }

    override suspend fun getList(
        offset: Int, order: SortOrder, filter: MangaListFilter
    ): List<Manga> {
        val url = "https://$domain/search/data".toHttpUrl().newBuilder().apply {
            addQueryParameter("limit", "32")
            addQueryParameter("offset", offset.toString())
            filter.query?.let {
                val query =
                    it.replace(Regex("""[^a-zA-Z0-9\s]"""), " ").replace(Regex("""\s+"""), " ")
                        .trim()
                addQueryParameter("text", query)
            }
            addQueryParameter(
                name = "sort",
                value = when (order) {
                    POPULARITY -> "Popularity"
                    UPDATED -> "Latest Updates"
                    else -> throw UnsupportedOperationException("unsupported order: $order")
                },
            )
            addQueryParameter(
                name = "order",
                value = when (order) {
                    POPULARITY, UPDATED -> "Descending"
                    else -> throw UnsupportedOperationException("unsupported order: $order")
                },
            )
            addQueryParameter("official", "Any")
            addQueryParameter("anime", "Any")
            filter.states.forEach { state ->
                addQueryParameter(
                    name = "included_status",
                    value = when (state) {
                        ONGOING -> "Ongoing"
                        FINISHED -> "Complete"
                        ABANDONED -> "Canceled"
                        PAUSED -> "Hiatus"
                        else -> throw UnsupportedOperationException("unsupported state: $state")
                    },
                )
            }
            filter.types.forEach { type ->
                addQueryParameter(
                    name = "included_type",
                    value = when (type) {
                        MANGA -> "Manga"
                        MANHWA -> "Manhwa"
                        MANHUA -> "Manhua"
                        COMICS -> "OEL"
                        else -> throw UnsupportedOperationException("unsupported type: $type")
                    },
                )
            }
            filter.tags.forEach { tag ->
                addQueryParameter("included_tag", tag.key)
            }
            addQueryParameter("display_mode", "Full Display")
        }.build()

        val document = webClient.httpGet(url).parseHtml()

        return document.select("article:has(section)").map { element ->

            val mangaId = element.selectFirstOrThrow("a").attrAsAbsoluteUrl("href")
                .toHttpUrl().pathSegments[1]

            val author =
                element.select("div:contains(Author(s): ) span a").eachText().joinToString()
                    .nullIfEmpty()

            val title =
                element.selectFirst("div.text-ellipsis.truncate.text-white.text-center.text-lg.z-20.w-\\[90\\%\\]")
                    ?.text() ?: "No name"
            Manga(
                id = generateUid(mangaId),
                url = mangaId,
                publicUrl = "https://$domain/series/$mangaId",
                title = title,
                coverUrl = element.selectFirst("picture img")?.attrAsAbsoluteUrlOrNull("src"),
                tags = element.selectFirst("div:contains(Tag(s): )")?.text()
                    ?.substringAfter("Tag(s): ")?.split(", ")?.mapToSet {
                        MangaTag(
                            title = it,
                            key = it,
                            source = source,
                        )
                    }.orEmpty(),
                state = when (document.selectFirst("div:contains(status) span")?.text()) {
                    "Ongoing" -> ONGOING
                    "Complete" -> FINISHED
                    "Canceled" -> ABANDONED
                    "Hiatus" -> PAUSED
                    else -> null
                },
                authors = setOfNotNull(author),
                largeCoverUrl = null,
                chapters = null,
                source = source,
            )
        }
    }

    override suspend fun getDetails(manga: Manga): Manga = coroutineScope {
        val document = webClient.httpGet("https://$domain/series/${manga.url}").parseHtml()

        val chapters = async { getChapters(manga.url, document) }

        val sectionLeft = document.select("section[x-data] > section")[0]
        val sectionRight = document.select("section[x-data] > section")[1]
        val author =
            sectionLeft.select("ul > li:has(strong:contains(Author)) > span > a").eachText()
                .joinToString()

        manga.copy(
            title = sectionRight.selectFirstOrThrow("h1").text(),
            coverUrl = sectionLeft.selectFirst("img")?.attrAsAbsoluteUrlOrNull("src"),
            tags = sectionLeft.select("ul > li:has(strong:contains(Tag)) a").mapToSet {
                MangaTag(
                    title = it.text(),
                    key = it.text(),
                    source = source,
                )
            },
            state = when (sectionLeft.selectFirst("ul > li:has(strong:contains(Status)) > a")
                ?.text()) {
                "Ongoing" -> ONGOING
                "Complete" -> FINISHED
                "Canceled" -> ABANDONED
                "Hiatus" -> PAUSED
                else -> null
            },
            authors = setOf(author),
            description = Element("div").also { desc ->
                sectionRight.selectFirst("li:has(strong:contains(Description)) > p")?.let {
                    desc.appendChild(it)
                }

                val ul = Element("ul")
                sectionLeft.select("ul > li:has(strong:contains(Track)) abbr").stream()
                    .forEach { abbr ->
                        abbr.selectFirst("a")?.attr("href")?.let { url ->
                            val a = Element("a").text(
                                abbr.attr("title"),
                            ).attr("href", url)

                            ul.appendChild(
                                Element("li").appendChild(a),
                            )
                        }
                    }

                if (ul.children().isNotEmpty()) {
                    desc.append("<br><strong>Links:</strong>")
                    desc.appendChild(ul)
                }
            }.text(),
            chapters = chapters.await(),
            source = source,
        )
    }

    override suspend fun getChapters(mangaId: String, mangaDocument: Document): List<MangaChapter> {
        val document =
            if (mangaDocument.selectFirst("#chapter-list > button[hx-get*=full-chapter-list]") != null) {
                webClient.httpGet("https://$domain/series/$mangaId/full-chapter-list").parseHtml()
            } else {
                mangaDocument
            }

        return document.select("div[x-data] > a").mapChapters(reversed = true) { i, element ->
            val chapterId = element.attrAsAbsoluteUrl("href").toHttpUrl().pathSegments[1]
            val name = element.selectFirstOrThrow("span.flex > span").text()

            MangaChapter(
                id = generateUid(chapterId),
                url = chapterId,
                title = name,
                number = Regex("""(?<!S)\b(\d+(\.\d+)?)\b""").find(name)?.groupValues?.get(1)
                    ?.toFloatOrNull() ?: i.toFloat(),
                volume = Regex("""(?:S|vol(?:ume)?)\s*(\d+)""").find(name)?.groupValues?.get(1)
                    ?.toInt() ?: 0,
                scanlator = when (element.selectFirst("svg")?.attr("stroke")) {
                    "#d8b4fe" -> "Official"
                    else -> null
                },
                uploadDate = dateFormat.tryParse(
                    element.selectFirst("time[datetime]")?.attr("datetime"),
                ),
                source = source,
            )
        }
    }

    @IgnoredOnParcel
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

    override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
        val url = "https://$domain".toHttpUrl().newBuilder().apply {
            addPathSegment("chapters")
            addPathSegment(chapter.url)
            addPathSegment("images")
            addQueryParameter("is_prev", "False")
            addQueryParameter("reading_style", "long_strip")
        }.build()

        val document = webClient.httpGet(url).parseHtml()

        return document.select("section[x-data~=scroll] > img").map { element ->
            val pageUrl = element.attrAsAbsoluteUrl("src")

            MangaPage(
                id = generateUid(pageUrl),
                url = pageUrl,
                source = source,
                chapterTitle = chapter.title ?: "",
            )
        }
    }

    companion object {
        private const val SOURCE_NAME = "WeebCentral"
        private const val OFFSET = 32
    }
}
