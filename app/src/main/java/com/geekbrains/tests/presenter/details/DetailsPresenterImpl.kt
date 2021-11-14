package com.geekbrains.tests.presenter.details

import com.geekbrains.tests.view.details.ViewDetailsContract

internal class DetailsPresenterImpl internal constructor(
    private var count: Int = 0
) : DetailsPresenterContract {

    private var viewContract: ViewDetailsContract? = null

    override fun attachView(view: ViewDetailsContract) {
        if (view != viewContract) {
            viewContract = view
        }
    }

    override fun detachView(view: ViewDetailsContract) {
        if (view == viewContract) {
            viewContract = null
        }
    }

    override fun setCounter(count: Int) {
        this.count = count
    }

    override fun onIncrement() {
        count++
        viewContract?.setCount(count)
    }

    override fun onDecrement() {
        count--
        viewContract?.setCount(count)
    }
}
