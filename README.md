# Civilian Cpr Android

Civilian Cpr Native Android application

## Features

- Light/dark mode from global device settings.
- Intercept sms messages.
- Persist sms messages from the cpr call number (hartstichting)
- Display push notification for the incoming cpr messages.
- When clicking the notification open google maps with the waypoints to the cpr location including possible aed waypoints.
- Display all messages in the app ordered by date with option to share, (batch) delete, copy or open directions in google maps.

## Tech used

- Kotlin.
- MVVM Architecture using Architecture Components.
- Single Activity with navigation between fragments using Navigation Components.
- Material design using Material Components.
- Dependency injection using Hilt.
- Local storage using Room with Coroutines for background operations.

## Screenshots


![Notification screenshot](https://github.com/kmartin0/assets/blob/master/civilian-cpr/Civilian_cpr_notification.png?raw=true) 
![Google Maps Directions screenshot](https://github.com/kmartin0/assets/blob/master/civilian-cpr/Civilian_cpr_google_maps.png?raw=true) 

![Empty State screenshot](https://github.com/kmartin0/assets/blob/master/civilian-cpr/Civilian_cpr_empty_state.png?raw=true) 
![Empty State Dark screenshot](https://github.com/kmartin0/assets/blob/master/civilian-cpr/Civilian_cpr_empty_state_dark.png?raw=true) 

![Messages screenshot](https://github.com/kmartin0/assets/blob/master/civilian-cpr/Civilian_cpr_messages.png?raw=true) 
![Messages Dark screenshot](https://github.com/kmartin0/assets/blob/master/civilian-cpr/Civilian_cpr_messages_dark.png?raw=true) 

![Messages Context Menu screenshot](https://github.com/kmartin0/assets/blob/master/civilian-cpr/Civilian_cpr_message_context_menu.png?raw=true) 
![Messages Delete Action Mode screenshot](https://github.com/kmartin0/assets/blob/master/civilian-cpr/Civilian_cpr_action_mode_delete.png?raw=true) 