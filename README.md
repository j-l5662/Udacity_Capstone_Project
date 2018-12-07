# Udacity_Capstone_Project / AWS Documentation Application
Capstone project for the Udacity Android Developer Course.

## Android Activities

<b>Main Activity</b>
This is the first activity that is presented to users. This activity lists out all of the AWS services to the user. A user can scroll through this activity and select a service to view additional details.

<b>Detail Activity</b>
This activity presents the breakdown of a specific service that they have selected. Users can select one to open up the Documentation Activity.

<b>Documentation Activity</b>
This activity presents the documentation page to the users. Users can scroll through the documentation and tap on the overflow menu to see related documentation pages. After a user enters this activity, the documentation page and the links that the users have navigated through are saved within the Room database. A user can utilize this app offline to view the documentation that they have previously viewed.

<b>Widget</b>
This widget stores the list of services that the user has selected. Clicking on one of these services opens the app into the Detail Activity for the selected AWS service.

## Third Party Libraries
 - ButterKnife (Bind Views):
 - Timber (Logging):
 - Volley (Network Calls):
 - Crashanalytics (Android crash aggregator):
 - Room (Database):
 - Jsoup (HTML Parsing):