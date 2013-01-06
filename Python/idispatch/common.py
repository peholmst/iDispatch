#!/usr/bin/env python3.3
# -*- coding: utf-8 -*-

import threading

class EventBus:
    """
    A simple, synchronous and thread-safe publish-subscribe event bus.
    """
    
    __subscribers_lock = threading.RLock()
    __subscribers = {}

    def publish(self, event_type, event):
        """
        Publishes the specified event to the subscribers that have subscribed
        to the specified event_type.
        """
        subscribers = []
        with self.__subscribers_lock:
            if event_type in self.__subscribers:
                subscribers = list(self.__subscribers[event_type])
        for callback in subscribers:
            callback(event)

    def subsribe(self, event_type, callback):
        """
        Subscribes the specified callback to the specified event_type. The
        callback function must take a single argument which will be the
        event object itself.
        """
        with self.__subscribers_lock:
            if not event_type in self.__subscribers:
                self.__subscribers[event_type] = [callback]
            else:
                self.__subscribers[event_type].append(callback)
    
    def unsubscribe(self, event_type, callback):
        """
        Unsubscribes the specified callback from the specified event_type.
        """
        with self.__subscribers_lock:
            if event_type in self.__subscribers:
                self.__subscribers[event_type].remove(callback)
                if not self.__subscribers[event_type]:
                    del self.__subscribers[event_type]
    


if __name__ == "__main__":
    # Very simple testing of the EventBus
    bus = EventBus()
    print(bus._EventBus__subscribers)
    
    def listener(event):
        print("Hello world %s" % event)
    
    def another_listener(event):
        print("Good bye world %s" % event)
    
    bus.subsribe("hello", listener)
    bus.subsribe("goodbye", another_listener)
    print(bus._EventBus__subscribers)
    
    bus.publish("hello", "joe cool")
    bus.publish("goodbye", "joe cool")
    
    bus.unsubscribe("hello", listener)
    bus.unsubscribe("goodbye", another_listener)
    print(bus._EventBus__subscribers)
