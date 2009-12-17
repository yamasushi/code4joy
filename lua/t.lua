require "Vector"
require "Matrix"

local a = Matrix.zero

print( a:uv() )

a:uv({{10,29},{25,66}})

print( a:uv() )
