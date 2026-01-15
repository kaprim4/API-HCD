package com.example.HCOData.validators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DateTimeValidator {


	public static boolean isNumeric(String str) {
		String[] parts = str.replace("'", "").split("");
		boolean result = true;
		for (String part : parts) {
			Pattern p = Pattern.compile("[0-9]");
			Matcher m = p.matcher(part);
			if (!m.matches()) {
				result = false;
			}

		}
		return result;
	}

	public static String normalizeString(String input) {
		return input.toUpperCase().replaceAll("[^A-Z0-9-]", "").replaceAll("\\s+", "");
	}

	public static String formatDate(LocalDateTime dateTime) {
		return dateTime.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
	}



	public static String validateDate(String datePA) {
		SimpleDateFormat dateFormat = null;

		if (datePA.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else if (datePA.matches("\\d{1,2}-\\d{1,2}-\\d{4}")) {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		} else if (datePA.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
			dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		} else if (datePA.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
			dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		} else {
			log.info(" Invalid date format : {} ", datePA);
			return "0000-00-00";
		}
		dateFormat.setLenient(false);

		try {
			Date date = dateFormat.parse(datePA);
			log.info(" Parsed Date : {} ", date);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);

			log.info(" Year is : {}", year);
			log.info(" Month is: {}", month);
			log.info(" Day is : {}", day);

			String dateFormatted = String.format("%04d-%02d-%02d", year, month, day);

			if (isValidDate(year, month, day)) {
				log.info(" Date is valid : {} ", dateFormatted);
				return dateFormatted;
			} else {
				log.info(" Invalid date components : {} ", dateFormatted);
				return "0000-00-00";
			}
		} catch (ParseException e) {
			log.info(" Invalid date format: {}", datePA);
			return "0000-00-00";
		}
	}

	public static boolean checkIsValidate(String date) {

		if (date.isEmpty() || date.equals(null)) {
			return false;
		}
		int day,mouth,years;
		String[] resDate;
		if (date.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			resDate = date.split("-");
			day = Integer.parseInt(resDate[2]);
			mouth = Integer.parseInt(resDate[1]);
			years = Integer.parseInt(resDate[0]);
			return isValidDate(years, mouth, day);
		} else if (date.matches("\\d{1,2}-\\d{1,2}-\\d{4}")) {
			resDate = date.split("-");
			day = Integer.parseInt(resDate[0]);
			mouth = Integer.parseInt(resDate[1]);
			years = Integer.parseInt(resDate[2]);
			return isValidDate(years, mouth, day);
		} else if (date.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
			resDate = date.split("/");
			day = Integer.parseInt(resDate[0]);
			mouth = Integer.parseInt(resDate[1]);
			years = Integer.parseInt(resDate[2]);
			return isValidDate(years, mouth, day);
		} else if (date.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
			resDate = date.split("/");
			day = Integer.parseInt(resDate[2]);
			mouth = Integer.parseInt(resDate[1]);
			years = Integer.parseInt(resDate[0]);
			return isValidDate(years, mouth, day);
		}
		return false;
	}

	private static boolean isValidDate(int year, int month, int day) {

		if (year < 1900 || year > 2100) {
			return false;
		}
		if (month < 1 || month > 12) {
			return false;
		}
		if (day < 1 || day > 31) {
			return false;
		}
		if (month == 2) {
			if (day > 29) {
				return false;
			}
			if (day == 29 && !isLeapYear(year)) {
				return false;
			}
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			if (day > 30) {
				return false;
			}
		}
		return true;
	}

	public static String convertHour(String hourPA) {
		String heurResult = "";
		String second = "";
		String minute = "";
		String hour = "";

		if (hourPA.length() == 5 || hourPA.length() == 3) {
			hourPA = "0" + hourPA;
		}
		if (hourPA.length() == 6 && hourPA != null) {
			second = hourPA.substring(4, 6);
			minute = hourPA.substring(2, 4);
			hour = hourPA.substring(0, 2);
			heurResult = hour + ":" + minute + ":" + second;
		} else if (hourPA.length() == 4) {
			minute = hourPA.substring(2, 4);
			hour = hourPA.substring(0, 2);
			heurResult = hour + ":" + minute + ":00";
		} else {
			heurResult = hourPA;
		}
		return heurResult;
	}


	public static boolean buyDateIsValid(String date) {
		DateTimeFormatter formatter = null;

		if (date.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		} else if (date.matches("\\d{1,2}-\\d{1,2}-\\d{4}")) {
			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		} else if (date.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
			formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		} else if (date.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
			formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		} else {
			return false;
		}

		try {
			LocalDate participationDate = LocalDate.parse(date, formatter);
			LocalDate currentDate = getCurrentDate();
			log.info("participationDate : {}, currentDate : {}", participationDate, currentDate);
			return isParticipationDateValid(participationDate, currentDate) && isDateWithin30Days(participationDate, currentDate) && StringUtils.isNoneEmpty(date);
		} catch (DateTimeParseException e) {
			log.info("Date parsing failed : {} ", e.getMessage());
			return false;
		}
	}

	public static LocalDate getCurrentDate() {
		return LocalDate.now();
	}

	public static boolean isParticipationDateValid(LocalDate participationDate, LocalDate currentDate) {
		return currentDate.isAfter(participationDate);
	}

	public static boolean isDateWithin30Days(LocalDate participationDate, LocalDate currentDate) {
		long daysBetween = ChronoUnit.DAYS.between(participationDate, currentDate);
		return daysBetween <= 30;
	}

	public static boolean matches(String str, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		boolean match = m.find();
		return match;
	}
	public static boolean checkIfDateExistEntreStartAndEndDate(String start_date, String end_date, String date) {
		if (date != null && start_date != null && end_date != null && matches(date, "\\d{4}\\-\\d{1,2}\\-\\d{1,2}")) {
			String[] startDateList = start_date.split("/");
			String[] endDateList = end_date.split("/");
			String[] dateList = date.split("-");
			int year, month, day;

			try {
				year = Integer.parseInt(dateList[0]);
				month = Integer.parseInt(dateList[1]);
				day = Integer.parseInt(dateList[2]);
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				return false;
			}

			if (month >= 1 && month <= 12 && day >= 1 && day <= Month.of(month).length(Year.isLeap(year))) {
				LocalDate startDate = LocalDate.of(Integer.parseInt(startDateList[2]),
						Integer.parseInt(startDateList[1]), Integer.parseInt(startDateList[0]));
				LocalDate endDate = LocalDate.of(Integer.parseInt(endDateList[2]), Integer.parseInt(endDateList[1]),
						Integer.parseInt(endDateList[0]));
				LocalDate dateToValidate = LocalDate.of(year, month, day);

				return (dateToValidate.isAfter(startDate) || dateToValidate.isEqual(startDate))
						&& (dateToValidate.isBefore(endDate) || dateToValidate.isEqual(endDate));
			}
		}
		return false;
	}

	private static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

}
