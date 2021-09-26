# iDispatch Station Alert
# Copyright (C) 2021 Petter Holmström 
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
RES_DIR := res
RES_OBJ_DIR := resobj

EXECUTABLE := $(BIN_DIR)/stationalert
SOURCES := $(wildcard $(SRC_DIR)/*.cpp)
OBJECTS := $(SOURCES:$(SRC_DIR)/%.cpp=$(OBJ_DIR)/%.o)
RESOURCES := $(wildcard $(RES_DIR)/*)
RESOURCE_OBJECTS := $(RESOURCES:$(RES_DIR)/%=$(RES_OBJ_DIR)/%.o)

CPPFLAGS := -Iinclude -MMD -MP
CXXFLAGS := -std=c++17 -Wall `pkg-config --cflags gtkmm-3.0`
LDFLAGS :=
LDLIBS := `pkg-config --libs gtkmm-3.0`
OBJCOPY := objcopy

.PHONY: all clean

all: $(EXECUTABLE)

$(EXECUTABLE): $(OBJECTS) $(RESOURCE_OBJECTS) | $(BIN_DIR)
	 $(CXX) $(LDFLAGS) $^ $(LDLIBS) -o $@

$(OBJ_DIR)/%.o: $(SRC_DIR)/%.cpp | $(OBJ_DIR)
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) -c $< -o $@

$(RES_OBJ_DIR)/%.o: $(RES_DIR)/% | $(RES_OBJ_DIR)
	$(OBJCOPY) --input binary --output elf64-x86-64 --binary-architecture i386:x86-64 $< $@

$(BIN_DIR) $(OBJ_DIR) $(RES_OBJ_DIR):
	mkdir -p $@

clean:
	@$(RM) -rv $(BIN_DIR) $(OBJ_DIR) $(RES_OBJ_DIR)

-include $(OBJ:.o=.d)
