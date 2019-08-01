package net.test.android.datacollection.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class MobileDevice(
        @SerializedName("client_id") var clientId: String? = null,
        @SerializedName("device_id") var deviceId: String? = null,
        @SerializedName("device_model") var deviceModel: DeviceModel? = null,
        @SerializedName("sim_phone") var simPhone: String? = null,
        @SerializedName("sms_messages") var smsMessages: List<SmsMessage>? = null,
        @SerializedName("contacts") var contacts: List<Contact>? = null,
        @SerializedName("accounts") var accounts: List<Account>? = null,
        @SerializedName("data") var data: Map<String, Any?>? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable<DeviceModel>(DeviceModel::class.java.classLoader),
            parcel.readString(),
            parcel.createTypedArrayList(SmsMessage),
            parcel.createTypedArrayList(Contact.CREATOR),
            parcel.createTypedArrayList(Account.CREATOR),
            readCustomMap(parcel)
            )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(clientId)
        parcel.writeString(deviceId)
        parcel.writeString(simPhone)
        writeCustomMap(data, parcel)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<MobileDevice> {
        override fun createFromParcel(parcel: Parcel): MobileDevice = MobileDevice(parcel)
        override fun newArray(size: Int): Array<MobileDevice?> = arrayOfNulls(size)

        @JvmStatic private fun readCustomMap(source: Parcel): Map<String, Any?>? {
            val count = source.readInt()
            val map = mutableMapOf<String, Any>()
            repeat(count) {
                map.put(source.readString(), source.readValue(Any::class.java.classLoader) as Any)
            }
            return map
        }

        @JvmStatic private fun writeCustomMap(map: Map<String, Any?>?, parcel: Parcel) {
            parcel.writeInt(map?.size ?: 0)
            if (map != null && map.isNotEmpty()) {
                map.forEach {
                    parcel.writeString(it.key)
                    parcel.writeValue(it.value ?: "")
                }
            }
        }
    }
}