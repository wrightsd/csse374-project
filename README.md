# csse374-project
Class project for CSSE 374 Team Laser Dinosaurs
This program is designed to generate a UML diagram based off of java source code.


Design:

Milestone 1:
The project has numerous ClassVisitors that visit the classes to get various pieces of information necessary to create the UML diagram.  These are all called from the DesignParser class, which does the most work.  This class contains the parse method, which loops through every class that is specified and generates the text to put in the GraphViz file.  The runner classes create a list of the classes to create UML for and the file to output the result to.

The ClassVisitors all just visit the specified class, and return a StringBuilder that is filled with the information they obtained from the class.  One ClassVisitor gets the declaration of the class, another gets whether the class is abstract, or an interface, one gets the fields, and the last one gets all the methods.  The DesignParser calls them in a specific order, and then combines their results to a StringBuilder that eventually gets output to the file.  The DesignParser is responsible to insert the text between fields or methods that are required for GraphViz processing that are not specified in the java code.

Any addition to any data that needs to be harvested for the UML diagram can be made by either changing just the ClassVisitor that obtains that piece of data or by creating a new ClassVisitor to perform the work.  Because each different type of data is split into its own ClassVisitor class, the ClassVisitors are a lot simpler and it is easier to tell which visitor does what, and modify any that need to be modified accordingly.  It is also easy to create new runners to generate a UML diagram for a different class and specify where it should be output to.

Milestone 2:
A separate ClassVisitor class was created to go through the classes to determine the uses and aggregates functionality.  This class calls a class that extends from the MethodVisitor class that creates the lists of uses and aggregation.  A singleton functionality was added to the DesignParser class that lets this MethodVisitor class determine which classes are part of the project that we are examining so that we do not add many extraneous arrows pointing towards external classes (e.g. String, Path etc.).  The lists that are generated from the MethodVisitor class are kept static so that each time a method is visited the program can ensure that it is not duplicating any of the previous arrows that were made from visiting other methods.


Who did what:

For the most part, we all worked together on every part of the code, alternating every 10 minutes to ensure that everyone worked on everything equally, and we were all paying attention when other group members were the one coding.  None of the methods or classes in the project can be accurately attributed to any single member of our group, as we all worked together on everything so far.  The only parts that we have done separately are as follows (separated by milestone):
Milestone 1:
  Tim Anderson: Wrote the README.md file
  Spencer Wright: Manually created a UML diagram for the project
  Gregory Nathan: Implemented the testing
Milestone 2:
  Tim Anderson: Updated the README.md file
  Spencer Wright: Updating UML diagram for the project
  Gregory Nathan: Updated testing implementation

Instructions:

Right now, we have created a separate runner class for each of the UML diagrams that need to be made.  To run the program, simply run the Lab1_3Runner class to generate the UML for the Lab1_3 or the UMLLaserDinosaursRunner class to generate a UML diagram for the project itself.  As the project currently stands, there is no way to pass a single file or java class to create a UML diagram, but this could be added in a later milestone without needing to modify any existing classes significantly.  Important note:  To run the lab1_3Runner, you must ensure that you have a java project with lab1_3 included in the run configuration.  Also, you may need to manually change the names of the files specified in the runner to match your local files because they are currently hard-coded to our version, which is included in the repository.
