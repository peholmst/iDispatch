# iDispatch Station Alert
# Copyright (C) 2021, 2022 Petter Holmström 
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

SRC_DIR := src
OBJ_DIR := obj
BIN_DIR := bin
TEST_DIR := test
TEST_OBJ_DIR := testobj
TEST_BIN_DIR := testbin

EXECUTABLE := $(BIN_DIR)/stationalert
SOURCES := $(wildcard $(SRC_DIR)/*.cpp)
OBJECTS := $(SOURCES:$(SRC_DIR)/%.cpp=$(OBJ_DIR)/%.o)
TESTS := $(wildcard $(TEST_DIR)/*.cpp)
TEST_OBJECTS := $(TESTS:$(TEST_DIR)/%.cpp=$(TEST_OBJ_DIR)/%.o)

CXXFLAGS := -Iinclude -MMD -MP -std=c++17 -Wall -DBOOST_LOG_DYN_LINK
LDFLAGS :=
LDLIBS := -lraylib -lGLESv2 -lEGL -lpthread -lgbm -ldrm -lboost_system -lboost_log -lssl -lcrypto
TEST_LDLIBS := $(LDLIBS) -lcppunit

.PHONY: all clean

all: $(EXECUTABLE)

$(EXECUTABLE): $(OBJECTS) | $(BIN_DIR)
	 $(CXX) $(LDFLAGS) $^ $(LDLIBS) -o $@

$(OBJ_DIR)/%.o: $(SRC_DIR)/%.cpp | $(OBJ_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(TEST_OBJ_DIR)/%.o: $(TEST_DIR)/%.cpp | $(TEST_OBJ_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(TEST_BIN_DIR)/%: $(TEST_OBJECTS) | $(TEST_BIN_DIR)
	$(CXX) $(LDFLAGS) $^ $(TEST_LDLIBS) -o $@

$(BIN_DIR) $(OBJ_DIR) $(TEST_OBJ_DIR) $(TEST_BIN_DIR):
	mkdir -p $@

tests: $(TEST_OBJECTS) $(filter-out $(OBJ_DIR)/main.o, $(OBJECTS)) | $(TEST_BIN_DIR)
	$(CXX) $(LDFLAGS) $^ $(TEST_LDLIBS) -o $(TEST_BIN_DIR)/AllTests
	$(TEST_BIN_DIR)/AllTests

clean:
	@$(RM) -rv $(BIN_DIR) $(OBJ_DIR) $(TEST_OBJ_DIR) $(TEST_BIN_DIR)

-include $(OBJ:.o=.d)
