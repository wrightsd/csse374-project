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

Milestone 3:
Diagram generation has been switched to following the strategy pattern. String arguments are passed into DesignParser with a second parameter to indicated which type of diagram to create. This second parameter is used in a HashMap to determine which DiagramMaker to utilize. The generateDiagramText method is called on the selected DiagramMaker to create the output text for either a sequence diagram or uml diagram and put it into a StringBuilder. As far as the actual implementation of sequence diagram text generation. A ClassMethodVisitor is used to track the visitng of all methods on the selected class until the specified method is visited. Then, a method visitor is created for this method. All other method calls within this method trigger the creation of an additional class visitor on the new class/method pair with a depth of one less. This repeats at increasing depths until the desired depth is reached.
Every time a new class is utilized, it is added to one StringBuilder tracking the classes necessary for the sequence diagram. Every time a new method is called it's correct signature is added to a StringBuilder holding all methods. These two are combined at the end to return the overall complete StringBuilder holding the text for the sequence diagram. 

Milestone 4:
Another ClassVisitor has been added, the SingletonClassVisitor, which decorates the previous ClassVisitor decoration and searches for the signs of a Singleton in the current class. If found, the class is nmarked as a singleton by adding an indicating string to a StringBuilder. This builder is later converted to a string and then a HashMap is used to determine the correct color for the pattern (or for no pattern at all). The color and pattern name are then appended to the overall string builder in the correct locations to cause desired look in the UML.

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
Milestone 3:
  Tim Anderson: Updated testing implementation
  Spencer Wright: Manual sequence diagrams
  Gregory Nathan: Updated README.md file
Milestone 4:
  Tim Anderson: Updated testing implementation
  Spencer Wright: Manual UML updates
  Gregory Nathan: README.md updates

Instructions:

Right now, we have created a separate runner class for each of the UML diagrams that need to be made.  To run the program, simply run the Lab1_3Runner class to generate the UML for the Lab1_3 or the UMLLaserDinosaursRunner class to generate a UML diagram for the project itself.  As the project currently stands, there is no way to pass a single file or java class to create a UML diagram, but this could be added in a later milestone without needing to modify any existing classes significantly.  Important note:  To run the lab1_3Runner, you must ensure that you have a java project with lab1_3 included in the run configuration.  Also, you may need to manually change the names of the files specified in the runner to match your local files because they are currently hard-coded to our version, which is included in the repository.

As of Milestone 3, all diagram creation is handled by DesignParser, and an additional parameter must be specified to indicated which diagram type to generate from the string array arguments. This parameter by default can be "uml" or "seq" for uml and sequence diagrams respectively. However, additional diagram types can be added and then selected using the addDiagramType method in Design Parser

To create the sequence diagram, the string arguments must be specified as [call depth, class name, method name, parameter1, parameter2, parameter3, ...]

The call depth is an optional parameter that can be any integer 0 or greater. If this is left out of the array the default call depth will be 5.

The class name is the full package path for the class (such as java.lang.String)

The method name is the regular String for the name

The remaining arguments are method parameter types, as many as necessary, using the full package path String.

Patterns currently detected: Singleton (blue)

For testing purposes, run the JUnit tests individually.  When the tests are all run at the same time errors sometime occur with synchronous evaluation reading a file before it gets fully written, making the JUnit test fail, whereas when all tests are ran individually they passed every time.
