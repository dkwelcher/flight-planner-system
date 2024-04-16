# Flight Planner System

The project was built by David Welcher, Joshua Rogers, and Andrew Nguyen for the University of South Carolina Aiken's CSCI A360 (Software Engineering) course during Spring 2023. The application is built entirely with Java as specified in the project requirements.

The project is a desktop application that allows users to manage airport, airplane, & nav beacon data and to plan flights between any two airports. The flight planner leverages the A* algorithm to determine the shortest path from starting airport to destination airport, while also considering the limitations of the selected airplane.

Run the main method in flight-planner-system/src/admin/Login.java to start the program.

## Features

- Graphical User Interface (GUI) using Java Swing
- Log In
- Internally controlled Registration
  -- Automatically generates username based on first & last name. Recursively appends identifier if username already exists.
  -- Access Level determines user access at the main menu
- Airport, Nav Beacon, & Airplane Managers
  -- Users can perform CRUD-related operations
- Flight Planner
  -- Uses A* algorithm that also considers limitations of the airplane
  -- Displays a detailed flight plan with all flight legs showing distance and hours travelled and cardinal direction.

## Updates Summary (04/16/2024)

Removed temporary testing files and added a default username password for log in. Added a README.md.

