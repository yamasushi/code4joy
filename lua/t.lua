require "Matrix"

print (Matrix.zero)
print (Matrix.unit)
local a = Matrix:new({-2,3},{1,2})
print(-a)
print(a(Vector:new(0,1)))
print(a(Vector:new(1,0)))

