package io.mustelidae.riverotter.domain.topic

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.Locale

@Document
class Topic(
    val name: String,
) {
    @Id
    var id: ObjectId = ObjectId()
        protected set

    var createdAt = LocalDateTime.now()!!
        protected set

    @DBRef
    var calendars: MutableList<TopicCalendar> = arrayListOf()
        protected set

    fun addBy(topicCalendar: TopicCalendar) {
        if (hasCalendar(topicCalendar.locale, topicCalendar.year))
            throw IllegalArgumentException("already exists topic calender")

        calendars.add(topicCalendar)
    }

    fun hasCalendar(locale: Locale, year: Int): Boolean {
        val calendar = this.calendars.find { it.locale == locale && it.year == year }
        return calendar != null
    }

    fun addCalendarIfNotExist(locale: Locale, year: Int) {
        if (hasCalendar(locale, year).not())
            addBy(TopicCalendar(locale, year))
    }

    fun getCalendar(locale: Locale, year: Int): TopicCalendar? {
        return this.calendars.find { it.locale == locale && it.year == year }
    }
}
