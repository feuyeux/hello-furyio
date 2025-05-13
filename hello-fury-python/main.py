import string
from typing import Dict
import pyfury

class SomeClass:
    name: str
    f1: "SomeClass"
    f2: Dict[str, str]
    f3: Dict[str, str]

fury = pyfury.Fury(ref_tracking=True)
fury.register_type(SomeClass)
obj = SomeClass()
obj.f2 = {"k1": "v1", "k2": "v2"}
obj.f1, obj.f3 = obj, obj.f2
obj.name= "hello"
data = fury.serialize(obj)
# bytes can be data serialized by other languages.
dObj = fury.deserialize(data)
print(dObj.name)