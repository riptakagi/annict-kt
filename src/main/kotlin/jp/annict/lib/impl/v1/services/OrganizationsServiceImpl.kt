package jp.annict.lib.impl.v1.services

import com.google.gson.reflect.TypeToken
import jp.annict.lib.interfaces.IAnnictClient
import jp.annict.lib.interfaces.services.OrganizationsGetRequestQuery
import jp.annict.lib.interfaces.services.OrganizationsGetResponseData
import jp.annict.lib.interfaces.services.OrganizationsService
import jp.annict.lib.utils.JsonUtil
import jp.annict.lib.impl.v1.enums.Order
import jp.annict.lib.impl.v1.models.Organization
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response

data class OrganizationsGetRequestQueryImpl (
    val fields           : Array<String>?=null,
    val filter_ids       : Array<Long>?=null,
    val filter_work_id   : Long?=null,
    val page             : Long?=null,
    val per_page         : Long?=null,
    val sort_id          : Order?=null) : OrganizationsGetRequestQuery {

    override fun url(builder: HttpUrl.Builder): HttpUrl {
        return builder.apply {
            addPathSegment("organizations")

            if(fields != null && fields.isNotEmpty()) { addQueryParameter("fields", fields.joinToString(separator = ",")) }
            if(filter_ids != null && filter_ids.isNotEmpty()) { addQueryParameter("filter_ids", filter_ids.joinToString(separator = ",")) }
            if(filter_work_id != null) { addQueryParameter("filter_work_id", filter_work_id.toString()) }
            if(page != null) { addQueryParameter("page", page.toString()) }
            if(per_page != null) { addQueryParameter("per_page", per_page.toString()) }
            if(sort_id != null) { addQueryParameter("sort_id", sort_id.name) }

        }.build()
    }
}

data class OrganizationsGetResponseDataImpl (
    val organizations: Array<Organization>?,
    val total_count: Long?,
    val next_page: Long?,
    val prev_page: Long?
) : OrganizationsGetResponseData {

    constructor() : this(null, null, null, null)

    override fun toDataClass(response: Response): OrganizationsGetResponseData {
        response.apply { JsonUtil.JSON_PARSER.parse(body?.string()).asJsonObject.apply { return OrganizationsGetResponseDataImpl (
            JsonUtil.GSON.fromJson(getAsJsonArray("organizations"), object : TypeToken<Array<Organization>>() {}.type),
            if (get("total_count").isJsonNull) null else get("total_count").asLong,
            if (get("next_page").isJsonNull) null else get("next_page").asLong,
            if (get("prev_page").isJsonNull) null else get("prev_page").asLong
        ) }
        }
    }
}

class OrganizationsServiceImpl(val client: IAnnictClient) : OrganizationsService {

    override fun get(query: OrganizationsGetRequestQuery) : OrganizationsGetResponseData {
        this.client.apply {
            return OrganizationsGetResponseDataImpl().toDataClass(request(Request.Builder().url(query.url(getUrlBuilder()))))
        }
    }

}