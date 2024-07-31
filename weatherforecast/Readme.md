#Weather Forecast

##Overview
Weather Forecast repository contains endpoints that enables us to process weather data from OpenWeather Public API. OpenWeather Public API provides the forecasted weather daywise nd hourwise.

#Technologies used
Weather Forecast repository is built using Java language and Springboot framework. Redis is used for caching the weather data thereby, reducing the performance time. Gradle is used for building the project.
The registered users are stored in Postgres database. Password encoder is BCrypt Password Encoder provided by Spring.

#List Of Endpoints

Weather Endpoints
- /api/weather/forecast/current - fetches the weather data for that moment
- /api/weather/forecast/timely - fetches data for every 3 hours
- /api/weather/forecast/daily - calculates average temperature and fetches other weather data for upcoming days
- /api/weather/forecast/threedays - fetches weather data and add additional details as per weather

Cache Endpoints:
/api/cache - fetches the cache keys
/api/cache/{key} - fetches the value for the given cache key

User Endpoints:
/api/user/register - registers a user and stored in database

#Design and Implementation Approach
Model-View-Controller architecture is used for this application.

We have a user controller which registers the users to the website. Users with encoded passwords are stored in database. The weather app endpoints are accessible with the user credentials. The weather responses are also stored in cache in order to reduce performance time.


