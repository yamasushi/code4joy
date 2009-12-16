require "Matrix"

local a = Matrix:rotate(math.rad(45))
print(-a)
print(a(Vector:new(0,1)))

