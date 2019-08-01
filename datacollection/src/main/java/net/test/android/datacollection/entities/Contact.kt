package net.test.android.datacollection.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Contact(
        @SerializedName("name") val name: String,
        @SerializedName("phone") val phone: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(phone)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact = Contact(parcel)
        override fun newArray(size: Int): Array<Contact?> = arrayOfNulls(size)
    }
}