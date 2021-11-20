package com.dixitgarg.investmentapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.dixitgarg.investmentapplication.utils.Utility
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.Serializable

@Entity(tableName = "InvestmentData", primaryKeys = ["MessageId"])
class Investment() : Parcelable {

    /*"time": "2021-09-26T15:48:34Z",
    "rounded_amount": 45,
    "place": "Manikanta",
    "spent_amount": 5,
    "msg_id": 6441938*/

    @ColumnInfo(name = "Time")
    var time = ""

    @ColumnInfo(name = "RoundedAmount")
    var roundedAmount = 0

    @ColumnInfo(name = "Place")
    var place = ""

    @ColumnInfo(name = "SpentAmount")
    var spentAmount = 0L

    @ColumnInfo(name = "MessageId")
    var messageId = 0L

    @ColumnInfo(name = "ConvertedTime")
    var convertedTime = ""

    constructor(parcel: Parcel) : this() {
        time = parcel.readString() ?: ""
        roundedAmount = parcel.readInt()
        place = parcel.readString() ?: ""
        spentAmount = parcel.readLong()
        messageId = parcel.readLong()
        convertedTime = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(time)
        parcel.writeInt(roundedAmount)
        parcel.writeString(place)
        parcel.writeLong(spentAmount)
        parcel.writeLong(messageId)
        parcel.writeString(convertedTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Investment> {

        fun mapWithJson(jsonObject: JSONObject): Investment {
            val investment = Investment()
            investment.time = jsonObject.optString("time")
            investment.roundedAmount = jsonObject.optInt("rounded_amount")
            investment.place = jsonObject.optString("place")
            investment.spentAmount = jsonObject.optLong("spent_amount")
            investment.messageId = jsonObject.optLong("msg_id")
            investment.convertedTime = Utility.convertStringToGivenDateFormat(investment.time, Utility.SERVER_DATE_FORMAT, "dd MMM yyyy")
            return investment
        }

        override fun createFromParcel(parcel: Parcel): Investment {
            return Investment(parcel)
        }

        override fun newArray(size: Int): Array<Investment?> {
            return arrayOfNulls(size)
        }
    }

}