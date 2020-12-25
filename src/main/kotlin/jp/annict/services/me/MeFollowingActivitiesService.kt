package jp.annict.services.me

import com.google.gson.reflect.TypeToken
import jp.annict.client.AnnictClient
import jp.annict.utils.JsonUtil
import jp.annict.enums.Action
import jp.annict.enums.Order
import jp.annict.models.Activity
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response

data class MeFollowingActivitiesGetRequestQuery (
    val fields: Array<String>? =null,
    val filter_actions: Array<Action>? =null,
    val filter_muted: Boolean? =null,
    val page: Long? =null,
    val per_page: Long? =null,
    val sort_id: Order?=null
) {

    fun url(builder: HttpUrl.Builder): HttpUrl {
        return builder.apply {
            addPathSegments("/me/following_activities")

            if(fields != null && fields.isNotEmpty()) { addQueryParameter("fields", fields.joinToString(separator = ",")) }
            if(filter_actions != null && filter_actions.isNotEmpty()) { addQueryParameter("filter_actions", filter_actions.joinToString(separator = ",")) }
            if(filter_muted != null) { addQueryParameter("filter_muted", filter_muted.toString()) }
            if(page != null) { addQueryParameter("page", page.toString()) }
            if(per_page != null) { addQueryParameter("per_page", per_page.toString()) }
            if(sort_id != null) { addQueryParameter("sort_id", sort_id.toString()) }

        }.build()
    }
}

data class MeFollowingActivitiesGetResponseData (
    val activities: Array<Activity>?,
    val total_count: Long?,
    val next_page: Long?,
    val prev_page: Long?
) {

    constructor() : this(null, null, null, null)

    fun toDataClass(response: Response): MeFollowingActivitiesGetResponseData {
        response.apply {
            JsonUtil.JSON_PARSER.parse(body?.string()).asJsonObject.apply { return MeFollowingActivitiesGetResponseData(
                JsonUtil.GSON.fromJson(
                    getAsJsonArray("activities"),
                    object : TypeToken<Array<Activity>>() {}.type
                ),
                if (get("total_count").isJsonNull) null else get("total_count").asLong,
                if (get("next_page").isJsonNull) null else get("next_page").asLong,
                if (get("prev_page").isJsonNull) null else get("prev_page").asLong
            )
            }
        }
    }
}

class MeFollowingActivitiesService (val client: AnnictClient) {

    fun get(query: MeFollowingActivitiesGetRequestQuery) : MeFollowingActivitiesGetResponseData {
        this.client.apply { return MeFollowingActivitiesGetResponseData()
            .toDataClass(request(Request.Builder().url(query.url(getUrlBuilder())))) }
    }

}