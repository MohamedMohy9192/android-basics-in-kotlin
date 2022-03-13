package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class OrderViewModel : ViewModel() {

    private val _quantity: MutableLiveData<Int> = MutableLiveData(0)
    val quantity: LiveData<Int> get() = _quantity

    private val _flavor: MutableLiveData<String> = MutableLiveData("")
    val flavor: LiveData<String> get() = _flavor

    private val _pickupDate: MutableLiveData<String> = MutableLiveData("")
    val pickupDate: LiveData<String> get() = _pickupDate

    private val _price: MutableLiveData<Double> = MutableLiveData(0.0)
    val price: LiveData<Double> get() = _price

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setData(pickupDate: String) {
        _pickupDate.value = pickupDate
    }

    /**
     * check if the flavor for the order has been set or not.
     */
    fun hasNoFlavorSet() = _flavor.value.isNullOrEmpty()
}