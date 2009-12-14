Vector = {}
Vector_mt = {__index = Vector}
function Vector:new(param_x,param_y)
	assert(param_x)
	assert(param_y)
	local o = {}
	o.x = param_x
	o.y = param_y
	return setmetatable(o,Vector_mt)
end

function Vector:zero()
	return Vector:new(0,0)
end

function Vector:dot(a,b)
	return a.x * b.x + a.y * b.y
end

function Vector:trig(theta)
	return Vector:new(math.cos(theta),math.sin(theta))
end

function Vector:abs()
	assert(self)
	return math.sqrt( Vector:dot(self,self) )
end

function Vector:xy()
	assert(self)
	return self.x,self.y
end

Vector_mt.__unm = function(a)   return Vector:new( -a.x , -a.y ) end
Vector_mt.__add = function(a,b) return Vector:new( a.x+b.x , a.y+b.y ) end
Vector_mt.__sub = function(a,b) return Vector:new( a.x-b.x , a.y-b.y ) end
Vector_mt.__tostring = function(a) return string.format("(%s,%s)",tostring(a.x),tostring(a.y)) end

Vector_mt.__mul = function(a,b)
	if( type(a)=="number" ) then
		return Vector:new( a*b.x , a*b.y )
	end
	if( type(b)=="number" ) then
		return Vector:new( b*a.x , b*a.y )
	end
	assert(false)
end

