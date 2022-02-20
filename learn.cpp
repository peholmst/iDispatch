#include <string>
#include <iostream>
#include <vector>
#include <memory>

using namespace std;

class Base
{
public:
    virtual void sayHello() { cout << "Hello, I'm base!"; }
};

class Derived : public Base
{
public:
    virtual void sayHello() { cout << "Hello, I'm derived!"; }
};

class Wrapper
{
public:
    //    Wrapper(const string &s) : s("") {}
    Wrapper(string &s) : s(s) {}
    friend ostream &operator<<(ostream &out, const Wrapper &wrapper);

private:
    string &s;
};

ostream &operator<<(ostream &out, const Wrapper &wrapper)
{
    out << wrapper.s << " (" << &wrapper.s << ")";
    return out;
}

unique_ptr<Base> makeDerived()
{
    return make_unique<Derived>(Derived());
}

int main(int argc, char **argv)
{
    string s("hello");
    Wrapper wrapper(s);

    vector<Wrapper> list;
    list.push_back(wrapper);

    s += " world";

    cout << s << "\n";
    cout << wrapper << "\n";
    cout << list[0] << "\n";
    cout << &wrapper << "\n";
    cout << &list[0] << "\n";

    unique_ptr<Base> derived = makeDerived();
    derived->sayHello();
    (*derived).sayHello();
    Base &ref = *(makeDerived());
    ref.sayHello();

    return 0;
}