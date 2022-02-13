**General Notes**

**The code does not need to compile, code just what is necessary for us to understand the relationships and communication between objects.**

**All code must be delivered in Java and assume that the user will not always input the correct information.**

**Requested diagrams can be done on paper and attached as pictures.**

**Exercise One**

1. Considering the structure of a school, create the diagram that describes the domain model and relationship between entities. Some of the possible entities may be Principal, Employee, Student and Janitor. Try to apply the concepts of Hierarchy, Abstract Class and Interface.

2. Implement a method that returns all the students in the school grouped by the first letter of their last name.

3. Implement a method that returns all students taking a subject. Please consider that the students cannot be repeated, this needs to be verified.

4. Create at least two different database table structure diagrams to describe the possible ways that the domain could be translated to the database. Explain pros and cons of each structure.

5. Having the following query:

   ```sql
   SELECT * FROM janitor j
   INNER JOIN employee e ON e.id = j.id
   INNER JOIN person p ON p.id = j.id
   WHERE j.workingArea = ‘Hallway’;
   ```

   Our query is taking too long to respond. What changes would you do to the query and/or the database to make it go faster if we only need the first and last name of the janitor?

6. Consider that we have a query that joins many tables and takes too long to return the values. We know as a fact that the tables involved do not update too often, but we still use this query many times in a day (consider it a report). What would you do to be able to get these results faster?
7. Using the model create a query that returns all the students with age between 19 and 21. Take into account that the age is not a column on the table, we have a field for birth date. How would you optimize this query?
8. We need to build a new application and we want to be able to have the business logic on the database engine instead of having it in our Java code. Can you suggest a way of doing this? As an example, how would you persist a student? What are the pros and cons of this?

**Exercise Two**

We need to create a Conference Rooms Booking application, knowing this:

1. Create the domain model diagram with all the possible entities, its attributes and relationships.
2. Deliver diagrams of the user interface for each step of a booking process.
3. Considering that meetings cannot last less than 15 minutes and more than 3 hours, also meetings cannot overlap with each other in the same room, deliver the validation method that is in charge to verify these rules.