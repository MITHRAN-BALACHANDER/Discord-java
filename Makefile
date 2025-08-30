# Discord Clone - Makefile
# Simple build system for Unix-like systems

# Java compiler
JC = javac
# Java runtime
JAVA = java
# Source directory
SRCDIR = src
# Binary directory
BINDIR = bin
# Main class
MAIN = Main

# Source files
SOURCES = $(SRCDIR)/utils/*.java $(SRCDIR)/models/*.java $(SRCDIR)/services/*.java $(SRCDIR)/$(MAIN).java

# Default target
all: compile

# Create bin directory
$(BINDIR):
	@echo "📁 Creating bin directory..."
	@mkdir -p $(BINDIR)

# Compile Java sources
compile: $(BINDIR)
	@echo "🔨 Compiling Java sources..."
	@$(JC) -d $(BINDIR) $(SOURCES)
	@echo "✅ Compilation complete!"

# Run the application
run: compile
	@echo "🎮 Starting Discord Clone..."
	@echo ""
	@echo "📋 Demo accounts:"
	@echo "   • admin / admin123 (Administrator)"
	@echo "   • moderator / mod123 (Moderator)"
	@echo "   • user / user123 (Regular User)"
	@echo ""
	@echo "==============================================="
	@echo ""
	@$(JAVA) -cp $(BINDIR) $(MAIN)

# Clean compiled files
clean:
	@echo "🧹 Cleaning compiled files..."
	@rm -rf $(BINDIR)
	@echo "✅ Clean complete!"

# Check Java installation
check:
	@echo "🔍 Checking Java installation..."
	@$(JAVA) -version
	@$(JC) -version

# Help target
help:
	@echo "Discord Clone - Build System"
	@echo ""
	@echo "Available targets:"
	@echo "  compile  - Compile Java sources"
	@echo "  run      - Compile and run the application"
	@echo "  clean    - Remove compiled files"
	@echo "  check    - Check Java installation"
	@echo "  help     - Show this help message"

# Phony targets
.PHONY: all compile run clean check help
