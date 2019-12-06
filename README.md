<h1> How to integrate </h1>

<h3> Maven: </h3>

Step 1: Add the JitPack Repository to your build file

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

Step 2: Add the dependency

	<dependency>
	    <groupId>com.github.BuggyFairy</groupId>
	    <artifactId>SwipeIt</artifactId>
	    <version>1.0</version>
	</dependency>

<h3> Gradle: </h3>

Step 1: Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  Step 2: Add the dependency
  
  dependencies {
	        implementation 'com.github.BuggyFairy:SwipeIt:1.0'
	}
  
  <h1> How to use </h1>
  
  <h4> Step 1: </h4> Create a list of RecyclerViewSwipeButton's:
  
  ```java
  List<RecyclerViewSwipeButton> buttonList = new ArrayList<>();
  ```
  
  <h4> Step 2: </h4> Add as many buttons as you want:
  
  The RecyclerViewSwipeButton has 4 parameters:
  
  - colorBackground : color for the background
  - icon : you can provide an icon as well
  - text : a text which will be displayed below the icon
  - clickListener: you can provide a callback which will be executed, when you click the button
  
  
  For example: 
  
  ```java
  RecyclerViewSwipeButton deleteBtn = new RecyclerViewSwipeButton(
                new ColorDrawable(ContextCompat.getColor(mMainTabActivity, R.color.statuslvl_red)),
                getDrawableImpl(R.drawable.ic_delete_white_36dp),
                new TextDrawable("Löschen"),
                new SwipeButtonClickListener() {
                    @Override
                    public void onClick(int position) {
                        // provide logic to execute on click
                    }
                }
        );
  ```
  
  <h4> Step 3: </h4> Add all the buttons to the list:
  
  For example:
  
  ```java
  buttonList.add(deleteBtn);
  ```
  
  <h4> step 4: </h4> Instantiate the RecyclerViewSwipeController
  
  You have to provide:
  
  - Context : is needed to create a gestureListener
  - recyclerView : the recyclerview whose entries shall swipe
  - buttonWidth : width of a button
  - buttonList : your created buttonlist
  
  For example:
  
  ```java
  new RecyclerViewSwipeController(mContext, mRecyclerView, 200, buttonList);
  ```
  
