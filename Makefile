
JCC = javac

JFLAGS = -g

JVM = java

MAIN = gatorTaxi

FILE = 

default: gatorTaxi.class


gatorTaxi.class: gatorTaxi.java
		$(JCC) $(JFLAGS) gatorTaxi.java
		

clean: 
		rm -f *.class