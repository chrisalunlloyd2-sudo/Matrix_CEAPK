package com.matrix.ce.core.viper

/**
 * SwarmPresenter: The "Brain" of the VIPER UI.
 * Coordinates data flow between the Interactor and the View.
 */
class SwarmPresenter(
    private val view: SwarmViewInput,
    private val interactor: SwarmInteractorInput,
    private val router: SwarmRouterInput
) : SwarmPresenterInput, SwarmInteractorOutput {

    override fun viewDidLoad() {
        view.showLoading()
        interactor.fetchStatus()
    }

    override fun didTapRefresh() {
        view.showLoading()
        interactor.fetchStatus()
    }

    // --- Interactor Output Handlers ---

    override fun onStatusFetched(statusList: List<SwarmStatus>) {
        view.hideLoading()
        view.showStatus(statusList)
    }

    override fun onError(message: String) {
        view.hideLoading()
        // In a full implementation, we would show a Toast or Alert
    }
}
