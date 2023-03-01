.286
.model tiny
.data
	the_number dw 5123,1231				; 2-byte number
	number_len dw $ - the_number
	ret_add     dw 0
	ret1_addr   dw 0
	mod10_res	dw 0
.code
org 100h
start:						; this time try to find mod10 of word number

	mov bx, the_number
	push bx
	pusha
	call mod10word
	popa
	pop dx
	add dl, '0'
	mov ah, 02h
	int 21h					; print the modulo to console
	mov ax, 4c00h
	int 21h					; return 0


mod10 proc
	mov word ptr mod10_res, 0
	pop dx					; save return address
	mov ret_add, dx
	pop cx					; len of BigNatural in bytes, always even
	cmp cx, 0
	jz fast_quit
	xor bx, bx				; bx is an index
loop_m:
	call mod10
	pop ax
	cmp bx, 0
	jz skip_mul
	; mul *6 TODO
skip_mul:
	add word ptr mod10_res, ax
	add bx, 2
	cmp cx, bx
	jne loop_m
fast_quit:
	mov dx, ret_add
	push dx
	ret
endp mod10

mod10word proc
	pop cx					; save return address
	pop ax					; take the divident from stack
	xor dx, dx				; ensure upper word is zero
	mov bx, 10
	div bx					; divide dxax by bx, ax=result, dx=remainder
	push dx
	push cx					; recover return address
	ret
endp mod10word

end	start
