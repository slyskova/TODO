# Pre-work - MyTODO

MyTODO is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: Sviatlana Lyskova

Time spent: ~30 hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can **successfully add and remove items** from the todo list
* [X] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [X] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [X] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [X] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [X] Add support for completion due dates for todo items (and display within listview item)
* [ ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [X] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [X] Add "Delete task" button (it's hidden while adding a new task)
* [X] Add "Delete all" feature with appropriate dialog
* [X] Add "Unsaved changes" dialog while pressing Up/Back buttons 
* [X] Add appropriate error message in case required "Name" field is empty
* [X] Add "Empty screen"

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

![Demo](http://i.imgur.com/MsEHrRK.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** [I used to work with Web Driver and UI Automator (wrote automated tests for Web and Android). For instance, there are many ways to find element in the Web Driver (by ID, by CLASS, by NAME, by CSS selector, by XPath, by TAG name), but only one way to find element in the Android. I used Java programming language to work with WebDriver, UI Automator and Android. In addition to that, IU Automator experience gave me a lot of knowledge about Android system in total].

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** [Array Adapter is an adapter that takes an array of items and uses this array to build our list. In my project I chose to use CursorAdapter. CursorAdapter takes a cursor which we receive as a result of a query to a sqlite database or a content resolver. We pass this cursor to the adapter and the list is created based upon the data present in the cursor.

ConvertView is a view that is currently not in the screen and hence can be recycled. Before that we should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, ConvertView method can create a new view.]

## Notes

Describe any challenges encountered while building the app.

## License

    Copyright [2017] [sviatlana_lyskova]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
