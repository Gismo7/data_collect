package net.test.android.datacollection.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class DeviceModel(
        @SerializedName("vendor") val vendor: String,
        @SerializedName("model") val model: String,
        @SerializedName("platform") val platform: String,
        @SerializedName("os_version") val osVersion: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(vendor)
        parcel.writeString(model)
        parcel.writeString(platform)
        parcel.writeString(osVersion)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<DeviceModel> {
        override fun createFromParcel(parcel: Parcel): DeviceModel = DeviceModel(parcel)

        override fun newArray(size: Int): Array<DeviceModel?> = arrayOfNulls(size)
    }
}
