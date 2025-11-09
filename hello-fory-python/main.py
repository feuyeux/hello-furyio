import string
from typing import Dict
import pyfory

class SomeClass:
    name: str
    f1: "SomeClass"
    f2: Dict[str, str]
    f3: Dict[str, str]

fory = pyfory.fory(ref_tracking=True)
fory.register_type(SomeClass)
obj = SomeClass()
obj.f2 = {"k1": "v1", "k2": "v2"}
obj.f1, obj.f3 = obj, obj.f2
obj.name= "hello"
data = fory.serialize(obj)
# bytes can be data serialized by other languages.
dObj = fory.deserialize(data)
print(dObj.name)