# iDispatch Messages
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

PROTOC_VERSION := 3.19.4
SRC_DIR := src
OBJ_DIR := obj
LIB_DIR := lib
INCLUDE_DIR := include
PROTOC_DIR=../../.tools/protobuf-$(PROTOC_VERSION)-bin

TARGET_LIBRARY := $(LIB_DIR)/libidispatch_messages.so
SOURCES := $(wildcard $(SRC_DIR)/*.cc)
OBJECTS := $(SOURCES:$(SRC_DIR)/%.cc=$(OBJ_DIR)/%.o)

CPPFLAGS := -MMD -MP -I$(PROTOC_DIR)/include
CXXFLAGS := -std=c++17 -Wall
LDFLAGS := -shared
LDLIBS := -lprotobuf -L$(PROTOC_DIR)/lib

.PHONY: all clean

all: $(TARGET_LIBRARY)

$(TARGET_LIBRARY): $(OBJECTS) | $(LIB_DIR)
	 $(CXX) $(LDFLAGS) $^ $(LDLIBS) -o $@

$(OBJ_DIR)/%.o: $(SRC_DIR)/%.cc | $(OBJ_DIR)
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) -c $< -o $@

$(LIB_DIR) $(OBJ_DIR):
	mkdir -p $@

clean:
	@$(RM) -rv $(LIB_DIR) $(OBJ_DIR)

-include $(OBJ:.o=.d)
