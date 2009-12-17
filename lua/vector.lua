Vector = {}
Vector_mt = {__index = Vector}
function Vector:new(param_x,param_y)
	assert(param_x)
	assert(param_y)
	local o = {param_x,param_y}
	o.x = o[1]
	o.y = o[2]
	return setmetatable(o,Vector_mt)
end

Vector.zero = Vector:new(0,0)

function Vector:dot(a,b)
	--print (a,b)
	return a[1]*b[1] + a[2]*b[2]
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
	return self[1] , self[2]
end

Vector_mt.__unm = function(a)   return Vector:new( -a[1] , -a[2] ) end
Vector_mt.__add = function(a,b) return Vector:new( a[1]+b[1] , a[2]+b[2] ) end
Vector_mt.__sub = function(a,b) return Vector:new( a[1]-b[1] , a[2]-b[2] ) end
Vector_mt.__tostring = function(a) return string.format("(%s,%s)",tostring(a[1]),tostring(a[2])) end

Vector_mt.__mul = function(a,b)
	if( type(a)=="number" ) then
		return Vector:new( a*b[1] , a*b[2] )
	end
	if( type(b)=="number" ) then
		return Vector:new( b*a[1] , b*a[2] )
	end
	assert(false)
end

Vector_mt.__div = function(a,b)
	if( type(b)=="number" ) then
		assert(b ~= 0)
		return Vector:new( a[1]/b , a[2]/b )
	end
	assert(false)
end
