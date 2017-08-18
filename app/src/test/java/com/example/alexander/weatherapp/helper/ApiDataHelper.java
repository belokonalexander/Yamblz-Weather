package com.example.alexander.weatherapp.helper;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.yamblz.voltek.weather.data.api.weather.models.WeatherResponseModel;
import com.yamblz.voltek.weather.data.api.weather.models.forecast.ForecastResponseModel;

public class ApiDataHelper {


    private static Gson getDefaultGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public interface Modify {
        void modifyJSON(JsonObject jsonObject);
    }

    public static <T> T getModel(Class<T> type) {
        return getModelFromJSON(type, getDefaultJSON(type));
    }

    public static <T> T getModel(Class<T> type, Modify... modify) {
        return getModelFromJSON(type, getAlteredWeatherModel(type, modify));
    }

    private static <T> T getModelFromJSON(Class<T> type, JsonObject json) {
        return getDefaultGson().fromJson(json.toString(), type);
    }

    private static  <T> JsonObject getAlteredWeatherModel(Class<T> type, Modify... modifies) {
        JsonObject jsonObject = getDefaultJSON(type);
        for (Modify modify : modifies) {
            modify.modifyJSON(jsonObject);
        }
        return jsonObject;
    }

    private static <T> JsonObject getDefaultJSON(Class<T> type) {
        if (type == WeatherResponseModel.class)
            return new JsonParser().parse(getWeatherModelJSON()).getAsJsonObject();
        else if (type == ForecastResponseModel.class) {
            return new JsonParser().parse(getForecastModelJSON()).getAsJsonObject();
        } else throw new RuntimeException("no class data foud");
    }

    private static String getWeatherModelJSON() {

        return "{\n" +
                "\"coord\": {\n" +
                "\"lon\": 37.62,\n" +
                "\"lat\": 55.75\n" +
                "},\n" +
                "\"weather\": [\n" +
                "  {\n" +
                "\"id\": 803,\n" +
                "\"main\": \"Clouds\",\n" +
                "\"description\": \"broken clouds\",\n" +
                "\"icon\": \"04d\"\n" +
                "}\n" +
                "],\n" +
                "\"base\": \"stations\",\n" +
                "\"main\": {\n" +
                "\"temp\": 292.9,\n" +
                "\"pressure\": 1015,\n" +
                "\"humidity\": 56,\n" +
                "\"temp_min\": 292.15,\n" +
                "\"temp_max\": 293.15\n" +
                "},\n" +
                "\"visibility\": 10000,\n" +
                "\"wind\": {\n" +
                "\"speed\": 3,\n" +
                "\"deg\": 250\n" +
                "},\n" +
                "\"clouds\": {\n" +
                "\"all\": 75\n" +
                "},\n" +
                "\"dt\": 1500541200,\n" +
                "\"sys\": {\n" +
                "\"type\": 1,\n" +
                "\"id\": 7325,\n" +
                "\"message\": 0.0206,\n" +
                "\"country\": \"RU\",\n" +
                "\"sunrise\": 1500513265,\n" +
                "\"sunset\": 1500573379\n" +
                "},\n" +
                "\"id\": 524901,\n" +
                "\"name\": \"Moscow\",\n" +
                "\"cod\": 200\n" +
                "}";

    }

    private static String getForecastModelJSON() {
        return "{\n" +
                "  \"cod\": \"200\",\n" +
                "  \"message\": 0.0442,\n" +
                "  \"cnt\": 5,\n" +
                "  \"list\": [\n" +
                "    {\n" +
                "      \"dt\": 1502280000,\n" +
                "      \"main\": {\n" +
                "        \"temp\": 23.92,\n" +
                "        \"temp_min\": 23.53,\n" +
                "        \"temp_max\": 23.92,\n" +
                "        \"pressure\": 1020.8,\n" +
                "        \"sea_level\": 1040.33,\n" +
                "        \"grnd_level\": 1020.8,\n" +
                "        \"humidity\": 46,\n" +
                "        \"temp_kf\": 0.39\n" +
                "      },\n" +
                "      \"weather\": [\n" +
                "        {\n" +
                "          \"id\": 801,\n" +
                "          \"main\": \"Clouds\",\n" +
                "          \"description\": \"облачно\",\n" +
                "          \"icon\": \"02d\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"clouds\": {\n" +
                "        \"all\": 24\n" +
                "      },\n" +
                "      \"wind\": {\n" +
                "        \"speed\": 3.17,\n" +
                "        \"deg\": 353.5\n" +
                "      },\n" +
                "      \"sys\": {\n" +
                "        \"pod\": \"d\"\n" +
                "      },\n" +
                "      \"dt_txt\": \"2017-08-09 12:00:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"dt\": 1502290800,\n" +
                "      \"main\": {\n" +
                "        \"temp\": 23.09,\n" +
                "        \"temp_min\": 22.83,\n" +
                "        \"temp_max\": 23.09,\n" +
                "        \"pressure\": 1020.61,\n" +
                "        \"sea_level\": 1040.13,\n" +
                "        \"grnd_level\": 1020.61,\n" +
                "        \"humidity\": 40,\n" +
                "        \"temp_kf\": 0.26\n" +
                "      },\n" +
                "      \"weather\": [\n" +
                "        {\n" +
                "          \"id\": 800,\n" +
                "          \"main\": \"Clear\",\n" +
                "          \"description\": \"ясно\",\n" +
                "          \"icon\": \"01d\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"clouds\": {\n" +
                "        \"all\": 0\n" +
                "      },\n" +
                "      \"wind\": {\n" +
                "        \"speed\": 2.96,\n" +
                "        \"deg\": 357.003\n" +
                "      },\n" +
                "      \"sys\": {\n" +
                "        \"pod\": \"d\"\n" +
                "      },\n" +
                "      \"dt_txt\": \"2017-08-09 15:00:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"dt\": 1502301600,\n" +
                "      \"main\": {\n" +
                "        \"temp\": 17.74,\n" +
                "        \"temp_min\": 17.61,\n" +
                "        \"temp_max\": 17.74,\n" +
                "        \"pressure\": 1020.76,\n" +
                "        \"sea_level\": 1040.51,\n" +
                "        \"grnd_level\": 1020.76,\n" +
                "        \"humidity\": 48,\n" +
                "        \"temp_kf\": 0.13\n" +
                "      },\n" +
                "      \"weather\": [\n" +
                "        {\n" +
                "          \"id\": 800,\n" +
                "          \"main\": \"Clear\",\n" +
                "          \"description\": \"ясно\",\n" +
                "          \"icon\": \"01n\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"clouds\": {\n" +
                "        \"all\": 0\n" +
                "      },\n" +
                "      \"wind\": {\n" +
                "        \"speed\": 2.55,\n" +
                "        \"deg\": 355.001\n" +
                "      },\n" +
                "      \"sys\": {\n" +
                "        \"pod\": \"n\"\n" +
                "      },\n" +
                "      \"dt_txt\": \"2017-08-09 18:00:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"dt\": 1502312400,\n" +
                "      \"main\": {\n" +
                "        \"temp\": 14.25,\n" +
                "        \"temp_min\": 14.25,\n" +
                "        \"temp_max\": 14.25,\n" +
                "        \"pressure\": 1021.48,\n" +
                "        \"sea_level\": 1041.23,\n" +
                "        \"grnd_level\": 1021.48,\n" +
                "        \"humidity\": 63,\n" +
                "        \"temp_kf\": 0\n" +
                "      },\n" +
                "      \"weather\": [\n" +
                "        {\n" +
                "          \"id\": 800,\n" +
                "          \"main\": \"Clear\",\n" +
                "          \"description\": \"ясно\",\n" +
                "          \"icon\": \"01n\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"clouds\": {\n" +
                "        \"all\": 0\n" +
                "      },\n" +
                "      \"wind\": {\n" +
                "        \"speed\": 2.46,\n" +
                "        \"deg\": 35.0083\n" +
                "      },\n" +
                "      \"sys\": {\n" +
                "        \"pod\": \"n\"\n" +
                "      },\n" +
                "      \"dt_txt\": \"2017-08-09 21:00:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"dt\": 1502323200,\n" +
                "      \"main\": {\n" +
                "        \"temp\": 10.94,\n" +
                "        \"temp_min\": 10.94,\n" +
                "        \"temp_max\": 10.94,\n" +
                "        \"pressure\": 1022.2,\n" +
                "        \"sea_level\": 1042.05,\n" +
                "        \"grnd_level\": 1022.2,\n" +
                "        \"humidity\": 83,\n" +
                "        \"temp_kf\": 0\n" +
                "      },\n" +
                "      \"weather\": [\n" +
                "        {\n" +
                "          \"id\": 800,\n" +
                "          \"main\": \"Clear\",\n" +
                "          \"description\": \"ясно\",\n" +
                "          \"icon\": \"01n\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"clouds\": {\n" +
                "        \"all\": 0\n" +
                "      },\n" +
                "      \"wind\": {\n" +
                "        \"speed\": 1.18,\n" +
                "        \"deg\": 58.504\n" +
                "      },\n" +
                "      \"sys\": {\n" +
                "        \"pod\": \"n\"\n" +
                "      },\n" +
                "      \"dt_txt\": \"2017-08-10 00:00:00\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"city\": {\n" +
                "    \"id\": 524901,\n" +
                "    \"name\": \"Moscow\",\n" +
                "    \"coord\": {\n" +
                "      \"lat\": 55.7522,\n" +
                "      \"lon\": 37.6156\n" +
                "    },\n" +
                "    \"country\": \"RU\"\n" +
                "  }\n" +
                "}";
    }

}
