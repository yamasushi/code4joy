Point = {}
mt = {}
function Vector:new(param_x,param_y)
	local o = {}
	o.x = param_x
	o.y = param_y
	return setmetatable(o,mt)
end
mt.__index = Vector
mt.__unm = function(a)   return Vector:new( -a.x , -a.y ) end
mt.__add = function(a,b) return Vector:new( a.x+b.x , a.y+b.y ) end
mt.__sub = function(a,b) return Vector:new( a.x-b.x , a.y-b.y ) end
mt.__tostring = function(a) return string.format("(%s,%s)",a.x,a.y) end

