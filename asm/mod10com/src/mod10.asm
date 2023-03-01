.model tiny
.data
	the_number db 5h,77h, 33h		; 2182405 decimal (Big Endian)
;	the_number db 2h,4h,5h,6h,7h	; 30165763074 decimal (big endian)
	number_len db $ - the_number
.code
org 100h
start:
	xor cx, cx				; cx stores sequential byte of BigNatural
	xor dx, dx				; x will store result modulo

loop_mod:
	xor ax, ax
	lea bx, the_number
	add bx, cx
	mov al, [bx]			; move N-th byte into al
	cmp cx, 0
	jz no_mul
	mov bl, 6
	mul bl
no_mul:
	mov bl, 10
	div bl					; divide ax by bl, al=result, ah=remainder
	add dl, ah
	inc cl
	cmp cl, number_len
	jne loop_mod
loop_mod_finish:			; perform final modulo
	xor ax, ax
	mov al, dl
	mov bl, 10
	div bl					; divide ax by bl, al=result, ah=remainder
	mov dl, ah
	add dl, '0'
	mov ah, 02h
	int 21h					; print the modulo to console

	mov dl, 10
	int 21h					; print newline
	mov ax, 4c00h
	int 21h					; return 0
end	start
