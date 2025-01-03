package org.digital101.simplewallet.business.network.neo

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import org.digital101.simplewallet.business.constants.DataStoreKeys
import org.digital101.simplewallet.business.constants.NEO_BANK_BASE_URL
import org.digital101.simplewallet.business.core.AppDataStore
import org.digital101.simplewallet.business.network.common.MainGenericResponse
import org.digital101.simplewallet.business.network.common.PaginatedGenericResponse
import org.digital101.simplewallet.business.network.neo.NeoService.Companion.ME
import org.digital101.simplewallet.business.network.neo.NeoService.Companion.MEMBERSHIP
import org.digital101.simplewallet.business.network.neo.NeoService.Companion.WALLET
import org.digital101.simplewallet.business.network.neo.responses.UserDataDTO
import org.digital101.simplewallet.business.network.neo.responses.WalletResponseDTO

class NeoServiceImpl(
    private val httpClient: HttpClient,
    private val appDataStore: AppDataStore,
) : NeoService {
    override suspend fun user(): MainGenericResponse<UserDataDTO?> {
        val token = appDataStore.readValue(DataStoreKeys.TOKEN) ?: ""
        return httpClient.get {
            url {
                headers { append(HttpHeaders.Authorization, token) }
                takeFrom(NEO_BANK_BASE_URL)
                encodedPath += ME
            }
        }.body()
    }

    override suspend fun updateUser(data: UserDataDTO): MainGenericResponse<UserDataDTO?> {
        val token = appDataStore.readValue(DataStoreKeys.TOKEN) ?: ""
        return httpClient.patch {
            url {
                headers { append(HttpHeaders.Authorization, token) }
                takeFrom(NEO_BANK_BASE_URL)
                encodedPath += "$MEMBERSHIP/${data.userId}"
            }
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

    override suspend fun wallet(): PaginatedGenericResponse<WalletResponseDTO> {
        val token = appDataStore.readValue(DataStoreKeys.TOKEN) ?: ""
        return httpClient.get {
            url {
                headers { append(HttpHeaders.Authorization, token) }
                takeFrom(NEO_BANK_BASE_URL)
                encodedPath += WALLET
            }
            contentType(ContentType.Application.Json)
        }.body()
    }
}