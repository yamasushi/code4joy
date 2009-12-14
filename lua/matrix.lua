require "Vector"

Matrix = {}
Matrix_mt = {__index = Matrix}
function Matrix:new(param_u,param_v)
	assert(param_u)
	assert(param_v)
	local o = {}
	o.u = param_u
	o.v = param_v
	return setmetatable(o,Matrix_mt)
end

function Matrix:transpose()
	assert(self)
	return Matrix:new( Vector:new( self.u.x , self.v.x ) , Vector:new( self.u.y , self.v.y ) )
end

Matrix_mt.__unm = function(a)   return Matrix:new( -a.u , -a.v ) end
Matrix_mt.__add = function(a,b) return Matrix:new( a.u+b.u , a.u+b.u ) end
Matrix_mt.__sub = function(a,b) return Matrix:new( a.v-b.v , a.v-b.v ) end
Matrix_mt.__tostring = function(a) return string.format("(%s,%s)", tostring(a.u) , tostring(a.v) ) end

Matrix_mt.__mul = function(a,b)
	if( type(a)=="number" ) then
		return Matrix:new( a*b.u , a*b.v )
	end
	if( type(b)=="number" ) then
		return Matrix:new( b*a.u , b*a.v )
	end

	local tb = b:transpose()
	return Matrix:new(
			Vector:new( Vector:dot(a.u,tb.u) , Vector:dot(a.u,tb.v) ) ,
			Vector:new( Vector:dot(a.v,tb.u) , Vector:dot(a.v,tb.v) ) )
end

function Matrix:rotate(t)
	local cos_t = math.cos(t)
	local sin_t = math.sin(t)
	return Matrix:new(
			Vector:new(cos_t,-sin_t),
			Vector:new(sin_t, cos_t))
end

function Matrix:scale(x,y)
	return Matrix:new(
			Vector:new(x,0),
			Vector:new(0,y))
end

function Matrix:unit()
	return Matrix:new(
			Vector:new(1,0) ,
			Vector:new(0,1) )
end

function Matrix:map()
	assert(self)
	local o = self
	return function(t)
		return Vector:new( Vector:dot(o.u , t) , Vector:dot(o.v , t) )
	end
end
