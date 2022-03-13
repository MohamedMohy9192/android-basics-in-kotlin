package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class OrderViewModel : ViewModel() {

    private val _quantity: MutableLiveData<Int> = MutableLiveData()
    val quantity: LiveData<Int> get() = _quantity

    private val _flavor: MutableLiveData<String> = MutableLiveData()
    val flavor: LiveData<String> get() = _flavor

    private val _date: MutableLiveData<String> = MutableLiveData()
    val date: LiveData<String> get() = _date

    private val _price: MutableLiveData<Double> = MutableLiveData()
    val price: LiveData<Double> get() = _price

    val dataOptions get() = getPickupOptions()

    init {
        // Initialize the properties when an instance of OrderViewModel is created.
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setData(pickupDate: String) {
        _date.value = pickupDate
    }

    /**
     * check if the flavor for the order has been set or not.
     */
    fun hasNoFlavorSet() = _flavor.value.isNullOrEmpty()

    /**
     * Build up a list of dates starting with the current date and the following three dates.
     * */
    fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        // This variable will contain the current date and time.
        val calendar = Calendar.getInstance()

        //  Because you'll need 4 date options, repeat this block of code 4 times.
        repeat(4) {
            // Format a date, add it to the list of date options
            options.add(formatter.format(calendar.time))
            //  Increment the calendar by 1 day.
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

    /**
     *  Reset the MutableLiveData properties in the view model. Assign the current date value
     *  from the dateOptions list to _date.value.
     * */
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dataOptions[0]
        _price.value = 0.0
    }
}