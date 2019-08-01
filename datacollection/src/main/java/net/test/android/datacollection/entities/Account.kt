package net.test.android.datacollection.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Account(
        @SerializedName("type") val type: String,
        @SerializedName("name") val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Account> {
        override fun createFromParcel(parcel: Parcel): Account = Account(parcel)

        override fun newArray(size: Int): Array<Account?> = arrayOfNulls(size)
    }
}