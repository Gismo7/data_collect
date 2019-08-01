package net.test.android.datacollection.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class SmsMessage(
        @SerializedName("message") val message: String,
        @SerializedName("address") val address: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeString(address)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SmsMessage> {
        override fun createFromParcel(parcel: Parcel): SmsMessage = SmsMessage(parcel)

        override fun newArray(size: Int): Array<SmsMessage?> = arrayOfNulls(size)
    }
}