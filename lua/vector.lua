Vector = {}
Vector_mt = {__index = Vector}
function Vector:new(param_x,param_y)
	local o = {}
	o.x = param_x
	o.y = param_y
	return setmetatable(o,Vector_mt)
end

function Vector:dot(a,b)
	return a.x * b.x + a.y * b.y
end

function Vector:abs()
	return math.sqrt( Vector:dot(self,self) )
end

Vector_mt.__unm = function(a)   return Vector:new( -a.x , -a.y ) end
Vector_mt.__add = function(a,b) return Vector:new( a.x+b.x , a.y+b.y ) end
Vector_mt.__sub = function(a,b) return Vector:new( a.x-b.x , a.y-b.y ) end
Vector_mt.__tostring = function(a) return string.format("(%s,%s)",tostring(a.x),tostring(a.y)) end

