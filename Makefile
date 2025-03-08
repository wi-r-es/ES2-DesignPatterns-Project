# Variables
SRC_DIR = src
OUT_DIR = out
JAR_NAME = MyApp.jar
MAIN_CLASS = src.com.es2.designpatterns.Main

# Java compiler and jar command
JAVAC = javac
JAVA = java
JAR = jar

# Compilation flags
JAVAC_FLAGS = -source 8 -target 8

# Default target
all: build run

# Build the application (compile Java files)
build:
	@echo "Compiling Java files..."
	@for /r $(SRC_DIR) %%f in (*.java) do $(JAVAC) $(JAVAC_FLAGS) -d $(OUT_DIR) %%f

# Run the application
run:
	@echo "Running the application..."
	$(JAVA) -cp $(OUT_DIR) $(MAIN_CLASS)

# Create a JAR file (includes the main class)
create-jar:
	@echo "Creating JAR file..."
	$(JAR) cvfe $(JAR_NAME) $(MAIN_CLASS) -C $(OUT_DIR) .

# Clean the build
clean:
	@echo "Cleaning up..."
	del /s /q $(OUT_DIR) $(JAR_NAME)

# Rebuild the app
rebuild: clean all
