require "vector"
require "frame"

Geometry = {}
Geometry_mt = {__index = Geometry}

function Geometry:new(param_frame)
	assert(param_frame)
	local o = {}
	o.frame = param_frame
	o.size  = o.frame:size()
	assert( o.size.x>0 )
	assert( o.size.y>0 )
	o.aspect_ratio = o.size.x / o.size.y
	return setmetatable(o,Geometry_mt)
end

Geometry_mt.__tostring = function(a)
	return string.format("(frame=%s,size=%s,aspect_ratio=%f)",
				tostring(a.frame) ,
				tostring(a.size)  ,
				a.aspect_ratio )
end

-- self ... canvas
function Geometry:canvas_transform(param_data_geom)
	assert(self)
	local img_geom = self
	--
	-- inflate data geometry
	local c = (param_data_geom.frame.min + param_data_geom.frame.max ) / 2
	local s = param_data_geom.size * 1.1
	local data_geom=Geometry:new(Frame:new( c - s/2 , c + s/2 ) )
	--
	local ratio = 0.0
	if(data_geom.aspect_ratio < img_geom.aspect_ratio)then
		ratio = img_geom.size.y / data_geom.size.y
	else
		ratio = img_geom.size.x / data_geom.size.x
	end
	assert(ratio>0.0)

	local origin = data_geom.frame.min * ratio
	local offset = -origin
	if ( data_geom.aspect_ratio < img_geom.aspect_ratio  ) then
		-- data     canvas
		--  **      *****
		--  ** ---> *****
		--  **      *****
		offset.x = offset.x + (img_geom.size.x - (data_geom.size.x*ratio))*0.5
	else
		-- data     canvas
		--	*****      **
		--	***** ---> **
		--	*****      **
		offset.y = offset.y + (img_geom.size.y - (data_geom.size.y*ratio))*0.5
	end

	return function(p)
			local q = p*ratio + offset
			q.y = img_geom.size.y - q.y
			return q
		end
	end



