ImageBuffer = {}
ImageBuffer_mt = {__index = ImageBuffer}

function ImageBuffer:new(img_width,img_height)
	assert(img_width)
	assert(img_height)
	local o    = {}
	o.width    = img_width
	o.height   = img_height
	o.histgram = {}
	return setmetatable(o,ImageBuffer_mt)
end

function ImageBuffer:update(x,y,v,op)
	assert(self)
	assert(self.histgram)
	assert(self.width)
	assert(self.height)
	--
	if( x <  0 ) then return 0 end
	if( x >= self.width ) then return 0 end
	if( y <  0 ) then return 0 end
	if( y >= self.height ) then return 0 end
	--
	local ix = math.floor(x+0.5)
	local iy = math.floor(y+0.5)
	--print(x,y)
	return self:update_cell(ix,iy,v,op)
end

function ImageBuffer:update_cell(ix,iy,v,op)
	assert(self)
	assert(self.histgram)
	assert(self.width)
	assert(self.height)
	--
	if( ix <  0 ) then return 0 end
	if( ix >= self.width ) then return 0 end
	if( iy <  0 ) then return 0 end
	if( iy >= self.height ) then return 0 end

	local current = 0.0
	if (self.histgram[ix] == nil) then
		self.histgram[ix]= {}
	end
	if ( self.histgram[ix][iy] ) then
		current = self.histgram[ix][iy]
	else
		current = 0.0
	end
	local result = (v+current)*0.5
	if(op)then op(result) end
	self.histgram[ix][iy] = result
	return result
end

function ImageBuffer:cell(ix,iy)
	assert(self)
	assert(self.histgram)
	assert(self.width)
	assert(self.height)
	--
	if( ix <  0 ) then return 0 end
	if( ix >= self.width ) then return 0 end
	if( iy <  0 ) then return 0 end
	if( iy >= self.height ) then return 0 end
	--
	if( self.histgram[ix] == nil ) then return 0 end
	if( self.histgram[ix][iy] == nil ) then return 0 end
	--
	return self.histgram[ix][iy]
end

function ImageBuffer:smooth()
	assert(self)
	assert(self.histgram)
	assert(self.width)
	assert(self.height)
	for ix = 0,self.width-1 do
		jx = ix - 1
		kx = ix + 1
		for iy = 0,self.height-1 do
			jy = iy - 1
			ky = iy + 1
			--
			self:update_cell(
				ix ,
				iy ,
				(	self:cell(ix,iy) +
					self:cell(ix,jy) +
					self:cell(ix,ky) +
					self:cell(jx,iy) +
					self:cell(jx,jy) +
					self:cell(jx,ky) +
					self:cell(kx,iy) +
					self:cell(kx,jy) +
					self:cell(kx,ky) ) / 9.0 )
		end
	end
end


function ImageBuffer:eachcell(op)
	assert(self)
	assert(self.histgram)
	--
	for ix,row in pairs(self.histgram) do
		--print("ix--"..ix)
		for iy,h in pairs(row) do
			op(ix,iy,h)
		end
	end
end
