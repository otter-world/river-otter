package io.mustelidae.riverotter.domain.calendar.holiday

import io.mustelidae.riverotter.config.AppEnvironment
import io.mustelidae.riverotter.config.NotSupportCountryException
import io.mustelidae.riverotter.domain.calendar.holiday.country.KoreaHoliday
import io.mustelidae.riverotter.domain.calendar.holiday.country.UnitedStateHoliday
import io.mustelidae.riverotter.domain.calendar.holiday.repository.HolidayCalendarRepository
import io.mustelidae.riverotter.domain.client.abstractapi.WorldHolidayClient
import io.mustelidae.riverotter.domain.client.korea.government.GovernmentOpenClient
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class HolidayCrawler(
    private val env: AppEnvironment,
    private val governmentOpenClient: GovernmentOpenClient,
    private val worldHolidayClient: WorldHolidayClient,
    private val holidayCalendarRepository: HolidayCalendarRepository,
) {
    fun crawling(year: Int, country: Locale): ObjectId {
        val countryHoliday = when (country) {
            Locale.KOREA -> KoreaHoliday(env.country.korea, governmentOpenClient, holidayCalendarRepository)
            Locale.US -> UnitedStateHoliday(worldHolidayClient, holidayCalendarRepository)
            else -> throw NotSupportCountryException(country)
        }

        return countryHoliday.create(year)
    }
}
