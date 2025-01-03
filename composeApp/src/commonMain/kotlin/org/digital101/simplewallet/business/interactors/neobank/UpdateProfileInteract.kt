package org.digital101.simplewallet.business.interactors.neobank

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.digital101.simplewallet.business.core.DataState
import org.digital101.simplewallet.business.core.NetworkState
import org.digital101.simplewallet.business.core.ProgressBarState
import org.digital101.simplewallet.business.core.UIComponent
import org.digital101.simplewallet.business.network.neo.NeoService
import org.digital101.simplewallet.business.network.neo.responses.UserDataDTO
import org.digital101.simplewallet.business.util.handleUseCaseException

class UpdateProfileInteract(
    private val service: NeoService,
) {
    fun execute(data: UserDataDTO): Flow<DataState<UserDataDTO>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.FullScreenLoading))
            if (data.userId != null) {
                val response = service.updateUser(data = data)

                response.message?.let {
                    emit(DataState.Response(uiComponent = UIComponent.None(it)))
                }

                val result = response.result
                if (result != null) {
                    emit(DataState.Data(result, status = response.status))
                }
            }
        } catch (e: Exception) {
            emit(DataState.NetworkStatus(NetworkState.Failed))
            emit(handleUseCaseException(e))
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}