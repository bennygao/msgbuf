/*---------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 NEC CORPORATION
 *
 *  ALL RIGHTS RESERVED BY NEC CORPORATION,  THIS PROGRAM
 *  MUST BE USED SOLELY FOR THE PURPOSE FOR WHICH IT WAS
 *  FURNISHED BY NEC CORPORATION,  NO PART OF THIS PROGRAM
 *  MAY BE REPRODUCED OR DISCLOSED TO OTHERS,  IN ANY FORM
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF NEC CORPORATION.
 *
 *      NEC CONFIDENTIAL AND PROPRIETARY
 *
 *---------------------------------------------------------------------------
 *
 * $Id: SQLParser.y,v 1.6 2003/05/27 04:54:42 gaobo Exp $
 *	  SQL Parser YACC rules/actions
 *
 * NOTES
 *	  CAPITALS are used to represent terminal symbols.
 *	  non-capitals are used to represent non-terminals.
 *
 *---------------------------------------------------------------------------
 */

%{
	import java.io.*;
%}

/* Reserved word tokens
 * SQL92 syntax has many type-specific constructs.
 */

/* Keywords (in SQL92 reserved words) */
%token	<obj>	KWD_ABSOLUTE KWD_ACTION KWD_ADD KWD_ALL KWD_ALTER 
		KWD_AND KWD_ANY KWD_AS KWD_ASC KWD_BEGIN_TRANS 
		KWD_BETWEEN KWD_BOTH KWD_BY KWD_CASCADE KWD_CASE 
		KWD_CAST KWD_CHAR KWD_CHARACTER KWD_CHECK KWD_CLOSE
		KWD_COALESCE KWD_COLLATE KWD_COLUMN KWD_COMMIT
		KWD_CONSTRAINT KWD_CONSTRAINTS KWD_CREATE KWD_CROSS 
		KWD_CURRENT KWD_CURRENT_DATE KWD_CURRENT_TIME 
		KWD_CURRENT_TIMESTAMP KWD_CURRENT_USER KWD_CURSOR 
		KWD_DAY_P KWD_DEC KWD_DECIMAL KWD_DECLARE KWD_DEFAULT 
		KWD_DELETE KWD_DESC KWD_DISTINCT KWD_DOUBLE KWD_DROP
		KWD_ELSE KWD_END_TRANS KWD_EXCEPT KWD_EXECUTE KWD_EXISTS 
		KWD_EXTRACT KWD_FALSE_P KWD_FETCH KWD_FLOAT KWD_FOR 
		KWD_FOREIGN KWD_FROM KWD_FULL KWD_GLOBAL KWD_GRANT 
		KWD_GROUP KWD_HAVING KWD_HOUR_P KWD_IN KWD_INNER_P 
		KWD_INSENSITIVE KWD_INSERT KWD_INTERSECT KWD_INTERVAL 
		KWD_INTO KWD_IS KWD_ISOLATION KWD_JOIN KWD_KEY KWD_DATE
		KWD_LANGUAGE KWD_LEADING KWD_LEFT KWD_LEVEL KWD_LIKE 
		KWD_LOCAL KWD_MATCH KWD_MINUTE_P KWD_MONTH_P KWD_NAMES
		KWD_NATIONAL KWD_NATURAL KWD_NCHAR KWD_NEXT KWD_NO 
		KWD_NOT KWD_NULLIF KWD_NULL_P KWD_NUMERIC KWD_OF KWD_ON 
		KWD_ONLY KWD_OPTION KWD_OR KWD_ORDER KWD_OUTER_P 
		KWD_OVERLAPS KWD_PARTIAL KWD_POSITION KWD_PRECISION 
		KWD_PRIMARY KWD_PRIOR KWD_PRIVILEGES KWD_PROCEDURE 
		KWD_PUBLIC KWD_READ KWD_REFERENCES KWD_RELATIVE KWD_REVOKE 
		KWD_RIGHT KWD_ROLLBACK KWD_SCROLL KWD_SECOND_P KWD_SELECT 
		KWD_SESSION_USER KWD_SET KWD_SOME KWD_SUBSTRING KWD_TABLE 
		KWD_TEMPORARY KWD_THEN KWD_TIME KWD_TIMESTAMP KWD_TIMEZONE_HOUR
		KWD_TIMEZONE_MINUTE KWD_TO KWD_TRAILING KWD_TRANSACTION 
		KWD_TRIM KWD_TRUE_P KWD_UNION KWD_UNIQUE KWD_UPDATE KWD_USER 
		KWD_USING KWD_VALUES KWD_VARCHAR KWD_VARYING KWD_VIEW
		KWD_WHEN KWD_WHERE KWD_WITH KWD_WORK KWD_YEAR_P KWD_ZONE

/* Keywords (in SQL3 reserved words) */
%token	<obj>	KWD_DEFERRABLE KWD_DEFERRED
		KWD_IMMEDIATE KWD_INITIALLY
		KWD_PENDANT KWD_RESTRICT
		KWD_TRIGGER

/* Keywords (in SQL92 non-reserved words) */
%token	<obj>	KWD_COMMITTED KWD_SERIALIZABLE KWD_TYPE_P

/* 
 * Keywords for Oracle 'select ... for update NOWAIT' option.
 *                                            ^^^^^^
 */
%token	<obj>	KWD_ORA_NOWAIT

/* Special keywords not in the query language - see the "lex" file */
%token	<obj>	SKWD_IDENT SKWD_FCONST SKWD_SCONST SKWD_Op
%token	<obj>	SKWD_ICONST SKWD_PARAM
%token	<obj>	SYM_COMMA SYM_LBRACKET SYM_RBRACKET

/* precedence */
%left		<obj>	KWD_OR
%left		<obj>	KWD_AND
%right		<obj>	KWD_NOT
%right		<obj>	OP_EQ OP_NOTEQ
%right		<obj>	SYM_DOLLAR
%nonassoc	<obj>	OP_ST OP_LT
%nonassoc	<obj>	KWD_LIKE
%nonassoc	<obj>	KWD_OVERLAPS
%nonassoc	<obj>	KWD_BETWEEN
%nonassoc	<obj>	KWD_IN
%left		<obj>	SKWD_Op	/* multi-character ops and user-defined operators */
%nonassoc	<obj>	KWD_NULL_P
%nonassoc	<obj>	KWD_IS
%left		<obj>	OP_PLUS OP_MINUS
%left		<obj>	SYM_STAR OP_DIV OP_MOD
%left		<obj>	OP_XOR
%left		<obj>	OP_OR	/* this is the relation union op, not logical or */

/* Unary Operators */
%right	<obj>	SYM_COLON
%left	<obj>	SYM_SEMICOLON		/* end of statement or natural log */
%right	<obj>	UMINUS
%left	<obj>	SYM_DOT
%left	<obj>	SYM_LSQB SYM_RSQB
%left	<obj>	KWD_UNION KWD_INTERSECT KWD_EXCEPT

%type	<obj>	statement;             
%type	<obj>	SQLStatement;          
%type	<obj>	InsertStmt;            
%type	<obj>	insert_begin;          
%type	<obj>	insert_rest;           
%type	<obj>	columnList;            
%type	<obj>	columnElem;            
%type	<obj>	DeleteStmt;            
%type	<obj>	delete_begin;          
%type	<obj>	UpdateStmt;            
%type	<obj>	set_phrase_begin;      
%type	<obj>	update_begin;          
%type	<obj>	SelectStmt;            
%type	<obj>	select_clause;         
%type	<obj>	select_union;          
%type	<obj>	select_union_opt;      
%type	<obj>	SubSelect;             
%type	<obj>	sub_select_begin;      
%type	<obj>	result;                
%type	<obj>	OptTempTableName;      
%type	<obj>	opt_table;             
%type	<obj>	opt_all;               
%type	<obj>	opt_distinct;          
%type	<obj>	sort_clause;           
%type	<obj>	sort_clause_begin;     
%type	<obj>	sortby_list;           
%type	<obj>	sortby;                
%type	<obj>	OptUseOp;              
%type	<obj>	name_list;             
%type	<obj>	group_clause;          
%type	<obj>	group_phrase_begin;    
%type	<obj>	having_clause;         
%type	<obj>	having_phrase_begin;   
%type	<obj>	for_update_clause;     
%type	<obj>	no_wait_opt;           
%type	<obj>	update_list;           
%type	<obj>	from_clause;           
%type	<obj>	from_phrase_begin;     
%type	<obj>	from_list;             
%type	<obj>	from_expr;             
%type	<obj>	table_expr;            
%type	<obj>	SubQueryBegin;         
%type	<obj>	alias_clause;          
%type	<obj>	join_clause;           
%type	<obj>	join_expr;             
%type	<obj>	join_clause_with_union;
%type	<obj>	join_expr_with_union;  
%type	<obj>	join_type;             
%type	<obj>	join_outer;            
%type	<obj>	join_qual;             
%type	<obj>	using_list;            
%type	<obj>	using_expr;            
%type	<obj>	where_clause;          
%type	<obj>	where_phrase_begin;    
%type	<obj>	relation_expr;         
%type	<obj>	opt_array_bounds;      
%type	<obj>	Typename;              
%type	<obj>	SimpleTypename;        
%type	<obj>	Generic;               
%type	<obj>	generic;               
%type	<obj>	Numeric;               
%type	<obj>	opt_float;             
%type	<obj>	opt_numeric;           
%type	<obj>	opt_decimal;           
%type	<obj>	Character;             
%type	<obj>	character;             
%type	<obj>	opt_varying;           
%type	<obj>	opt_charset;           
%type	<obj>	Datetime;              
%type	<obj>	datetime;              
%type	<obj>	opt_timezone;          
%type	<obj>	opt_interval;          
%type	<obj>	row_expr;              
%type	<obj>	row_descriptor;        
%type	<obj>	row_list;              
%type	<obj>	sub_type;              
%type	<obj>	all_Op;                
%type	<obj>	MathOp;                
%type	<obj>	a_expr;                
%type	<obj>	b_expr;                
%type	<obj>	c_expr;                
%type	<obj>	opt_indirection;       
%type	<obj>	expr_list;             
%type	<obj>	extract_list;          
%type	<obj>	extract_arg;           
%type	<obj>	position_list;         
%type	<obj>	substr_list;           
%type	<obj>	substr_from;           
%type	<obj>	substr_for;            
%type	<obj>	trim_list;             
%type	<obj>	in_expr_nodes;         
%type	<obj>	case_expr;             
%type	<obj>	when_clause_list;      
%type	<obj>	when_clause;           
%type	<obj>	case_default;          
%type	<obj>	case_arg;              
%type	<obj>	attr;                  
%type	<obj>	oracle_outer;          
%type	<obj>	attrs;                 
%type	<obj>	target_list;           
%type	<obj>	target_el;             
%type	<obj>	update_target_list;    
%type	<obj>	update_target_el;      
%type	<obj>	relation_name;         
%type	<obj>	attr_name;             
%type	<obj>	name;                  
%type	<obj>	func_name;             
%type	<obj>	AexprConst;            
%type	<obj>	ParamNo;               
%type	<obj>	Iconst;                
%type	<obj>	Sconst;                
%type	<obj>	ColId;                 
%type	<obj>	TokenId;               
%type	<obj>	ColLabel;              
%type	<obj>	SpecialRuleRelation;   

%%
statement: SQLStatement
	{
		System.out.println($1);
	}
	;

SQLStatement: SelectStmt
	{
	}		
	| InsertStmt
	{
	}		
	| UpdateStmt
	{
	}		
	| DeleteStmt
	{
	}		
	;

/*****************************************************************************
 *
 *		QUERY:
 *				INSERT STATEMENTS
 *
 *****************************************************************************/

InsertStmt: insert_begin insert_rest
	{
	}		
	;

insert_begin: KWD_INSERT KWD_INTO relation_name
	{
	}
	;

insert_rest: KWD_VALUES 
	{
	}
	SYM_LBRACKET target_list SYM_RBRACKET
	{
	}		
	| KWD_DEFAULT KWD_VALUES
	{
	}		
	| SelectStmt
	{
	}		
	| SYM_LBRACKET columnList SYM_RBRACKET KWD_VALUES
	{
	}
	  SYM_LBRACKET target_list SYM_RBRACKET
	{
	}		
	| SYM_LBRACKET columnList SYM_RBRACKET SelectStmt
	{
	}		
	;

columnList: columnList SYM_COMMA columnElem
	{
	}		
	| columnElem
	{
	}		
	;

columnElem: ColId opt_indirection
	{
	}		
	| ColId SYM_DOT ColId
	{
	}
	;


/*****************************************************************************
 *
 *		QUERY:
 *				DELETE STATEMENTS
 *
 *****************************************************************************/

DeleteStmt:  delete_begin table_expr where_clause
	{
	}		
	;

delete_begin: KWD_DELETE KWD_FROM
	{
	}
	;

/*****************************************************************************
 *
 *		QUERY:
 *				UPDATE STATEMENTS
 *
 *****************************************************************************/

UpdateStmt:  update_begin set_phrase_begin update_target_list from_clause where_clause
	{
	}		
	;

set_phrase_begin: KWD_SET
	{
	}
	;
	

update_begin: KWD_UPDATE relation_name
	{
	}
	;

/*****************************************************************************
 *
 *		QUERY:
 *				SELECT STATEMENTS
 *
 *****************************************************************************/

/* SelectStmt:	  select_clause sort_clause for_update_clause */
SelectStmt:	  select_clause
	{
	}		
	;

select_clause: SYM_LBRACKET select_clause SYM_RBRACKET
	{
	}		
	| select_union
	{
	}
	; 

select_union: SubSelect
	{
	}
	| SubSelect select_union_opt select_union
	{
	}
	;

select_union_opt: KWD_UNION opt_all
	{
	}
	| KWD_EXCEPT
	{
	}
	| KWD_INTERSECT
	{
	}
	;

SubSelect:	sub_select_begin opt_distinct target_list result from_clause where_clause group_clause having_clause sort_clause for_update_clause
	{
	}
	;

sub_select_begin: KWD_SELECT
	{
	}
	;


result:  KWD_INTO OptTempTableName			
	{
	}
	| /*EMPTY*/						
	{
	}
	;

OptTempTableName:  KWD_TEMPORARY opt_table relation_name
	{
	}
	| KWD_LOCAL KWD_TEMPORARY opt_table relation_name
	{
	}
	| KWD_GLOBAL KWD_TEMPORARY opt_table relation_name
	{
	}
	| KWD_TABLE relation_name
	{
	}
	| relation_name
	{
	}
	;

opt_table:  KWD_TABLE								
	{
	}
	| /*EMPTY*/								
	{
	}
	;

opt_all:  KWD_ALL									
	{
	}
	| /*EMPTY*/								
	{
	}
	;

opt_distinct:  KWD_DISTINCT							
	{
	}
	| KWD_DISTINCT KWD_ON SYM_LBRACKET expr_list SYM_RBRACKET			
	{
	}
	| KWD_ALL									
	{
	}
	| /*EMPTY*/								
	{
	}
	;

sort_clause: sort_clause_begin sortby_list
	{
	}
	| /*EMPTY*/								
	{
	}
	;

sort_clause_begin: KWD_ORDER KWD_BY
	{
	}
	;

sortby_list:  sortby							
	{
	}
	| sortby_list SYM_COMMA sortby				
	{
	}
	;

sortby: a_expr OptUseOp
	{
	}
	;

OptUseOp:  KWD_USING all_Op							
	{
	}
	| KWD_ASC									
	{
	}
	| KWD_DESC									
	{
	}
	| /*EMPTY*/								
	{
	}
	;

name_list:  name
	{
	}
			
	| name_list SYM_COMMA name
	{
	}
			
	;

group_clause:  group_phrase_begin expr_list				
	{
	}
	| /*EMPTY*/								
	{
	}
	;

group_phrase_begin: KWD_GROUP KWD_BY
	{
	}
	;

having_clause:  having_phrase_begin a_expr
	{
	}
			
	| /*EMPTY*/								
	{
	}
	;

having_phrase_begin: KWD_HAVING
	{
	}
	;

for_update_clause:  KWD_FOR KWD_UPDATE update_list no_wait_opt
	{
	}
	| KWD_FOR KWD_READ KWD_ONLY
	{
	}
	| /* EMPTY */							
	{
	}
	;

no_wait_opt: KWD_ORA_NOWAIT
	{
	}
	| /* EMPTY */
	{
	}
	;


update_list:  KWD_OF name_list
	{
	}
	| /* EMPTY */							
	{
	}
	;

from_clause:  from_phrase_begin from_list					
	{
	}
	| from_phrase_begin from_expr
	{
	}
	| /*EMPTY*/								
	{
	}
	;

from_phrase_begin: KWD_FROM
	{
	}
	;

from_list:  from_list SYM_COMMA table_expr
	{
	}
	| table_expr							
	{
	}
	;

from_expr:  SYM_LBRACKET join_clause_with_union SYM_RBRACKET alias_ident alias_clause
	{
	}
			
	| join_clause
	{
	}
	;

alias_ident: KWD_AS
	{
	}
	|
	;

table_expr:  relation_expr alias_ident alias_clause
	{
	}

/**
 * This rule is added to support the statement like following:
 * select * from (select * from tablename) TableAlias
 */
	| SubQueryBegin SubSelect SubQueryEnd alias_ident alias_clause
	{
	}
	;

SubQueryBegin: SYM_LBRACKET
	{
	} 
	;

SubQueryEnd: SYM_RBRACKET
	{
	}
	;


alias_clause:  ColId SYM_LBRACKET name_list SYM_RBRACKET
	{
	}
			
	| ColId
	{
	}
			
	| /*EMPTY*/
	{
	}
			
	;

join_clause:  join_clause join_expr
	{
	}
			
	| table_expr join_expr
	{
	}
			
	;

join_expr:  join_type KWD_JOIN table_expr join_qual
	{
	}
			
	| KWD_NATURAL join_type KWD_JOIN table_expr
	{
	}
			
	| KWD_CROSS KWD_JOIN table_expr
	{
	}
			
	;

join_clause_with_union:  join_clause_with_union join_expr_with_union
	{
	}
			
	| table_expr join_expr_with_union
	{
	}
			
	;

join_expr_with_union:  join_expr
	{
	}
			
	| KWD_UNION KWD_JOIN table_expr
	{
	}
			
	;

/* OUTER is just noise... */
join_type:  KWD_FULL join_outer						
	{
	}
	| KWD_LEFT join_outer
	{
	}
	| KWD_RIGHT join_outer						
	{
	}
	| KWD_OUTER_P								
	{
	}
	| KWD_INNER_P								
	{
	}
	| /*EMPTY*/								
	{
	}
	;

join_outer:  KWD_OUTER_P							
	{
	}
	| /*EMPTY*/								
	{
	}
	;

join_qual:  KWD_USING SYM_LBRACKET using_list SYM_RBRACKET			
	{
	}
	| KWD_ON a_expr								
	{
	}
	;

using_list:  using_list SYM_COMMA using_expr			
	{
	}
	| using_expr							
	{
	}
	;

using_expr:  ColId
	{
	}
	;

where_clause:  where_phrase_begin a_expr
	{
	}
	| /*EMPTY*/								
	{
	}
	;

where_phrase_begin: KWD_WHERE
	{
	}
	;

relation_expr:	relation_name
	{
	}
			
	| relation_name SYM_STAR				  %prec OP_EQ
	{
	}
			
	;

opt_array_bounds:	SYM_LSQB SYM_RSQB opt_array_bounds
	{
	}
			
	| SYM_LSQB Iconst SYM_RSQB opt_array_bounds
	{
	}
			
	| /*EMPTY*/
	{
	}
			
	;


/*****************************************************************************
 *
 *	Type syntax
 *		SQL92 introduces a large amount of type-specific syntax.
 *		Define individual clauses to handle these cases, and use
 *		 the generic case to handle regular type-extensible syntax.
 *
 *****************************************************************************/

Typename:  SimpleTypename opt_array_bounds
	{
	}
	;

SimpleTypename:  Generic
	{
	}
	| Numeric
	{
	}
	| Character
	{
	}
	| Datetime
	{
	}
	;

Generic:  generic
	{
	}
			
	;

generic:  SKWD_IDENT									
	{
	}
	| KWD_TYPE_P								
	{
	}
	;

/* SQL92 numeric data types
 * Check KWD_FLOAT() precision limits assuming IEEE floating types.
 * Provide real KWD_DECIMAL() and KWD_NUMERIC() implementations.
 */
Numeric:  KWD_FLOAT opt_float
	{
	}
			
	| KWD_DOUBLE KWD_PRECISION
	{
	}
			
	| KWD_DECIMAL opt_decimal
	{
	}
			
	| KWD_DEC opt_decimal
	{
	}
			
	| KWD_NUMERIC opt_numeric
	{
	}
			
	;

opt_float:  SYM_LBRACKET Iconst SYM_RBRACKET
	{
	}
			
	| /*EMPTY*/
	{
	}
			
	;

opt_numeric:  SYM_LBRACKET Iconst SYM_COMMA Iconst SYM_RBRACKET
	{
	}
			
	| SYM_LBRACKET Iconst SYM_RBRACKET
	{
	}
			
	| /*EMPTY*/
	{
	}
			
	;

opt_decimal:  SYM_LBRACKET Iconst SYM_COMMA Iconst SYM_RBRACKET
	{
	}
			
	| SYM_LBRACKET Iconst SYM_RBRACKET
	{
	}
			
	| /*EMPTY*/
	{
	}
			
	;


/*
 * SQL92 character data types
 * The following implements KWD_CHAR() and KWD_VARCHAR().
 */
Character:  character SYM_LBRACKET Iconst SYM_RBRACKET
	{
	}
			
	| character
	{
	}
			
	;

character:  KWD_CHARACTER opt_varying opt_charset
	{
	}
			
	| KWD_CHAR opt_varying						
	{
	}
	| KWD_VARCHAR								
	{
	}
	| KWD_NATIONAL KWD_CHARACTER opt_varying		
	{
	}
	| KWD_NATIONAL KWD_CHAR opt_varying				
	{
	}
	| KWD_NCHAR opt_varying						
	{
	}
	;

opt_varying:  KWD_VARYING							
	{
	}
	| /*EMPTY*/								
	{
	}
	;

opt_charset:  KWD_CHARACTER KWD_SET ColId				
	{
	}
	| /*EMPTY*/								
	{
	}
	;

Datetime: KWD_DATE
	{
	}

	| datetime
	{
	}
			
	| KWD_TIMESTAMP opt_timezone
	{
	}
			
	| KWD_TIME opt_timezone
	{
	}
			
	| KWD_INTERVAL opt_interval
	{
	}
			
	;

datetime:  KWD_YEAR_P								
	{
	}
	| KWD_MONTH_P								
	{
	}
	| KWD_DAY_P									
	{
	}
	| KWD_HOUR_P								
	{
	}
	| KWD_MINUTE_P								
	{
	}
	| KWD_SECOND_P								
	{
	}
	;

opt_timezone:  KWD_WITH KWD_TIME KWD_ZONE					
	{
	}
	| /*EMPTY*/								
	{
	}
	;

opt_interval:  datetime							
	{
	}
	| KWD_YEAR_P KWD_TO KWD_MONTH_P						
	{
	}
	| KWD_DAY_P KWD_TO KWD_HOUR_P						
	{
	}
	| KWD_DAY_P KWD_TO KWD_MINUTE_P						
	{
	}
	| KWD_DAY_P KWD_TO KWD_SECOND_P						
	{
	}
	| KWD_HOUR_P KWD_TO KWD_MINUTE_P					
	{
	}
	| KWD_HOUR_P KWD_TO KWD_SECOND_P					
	{
	}
	| KWD_MINUTE_P KWD_TO KWD_SECOND_P					
	{
	}
	| /*EMPTY*/								
	{
	}
	;


/*****************************************************************************
 *
 *	expression grammar
 *
 *****************************************************************************/

row_expr: SYM_LBRACKET row_descriptor SYM_RBRACKET KWD_IN SYM_LBRACKET SubSelect SYM_RBRACKET
	{
	}
			
	| SYM_LBRACKET row_descriptor SYM_RBRACKET KWD_NOT KWD_IN SYM_LBRACKET SubSelect SYM_RBRACKET
	{
	}
			
	| SYM_LBRACKET row_descriptor SYM_RBRACKET all_Op sub_type SYM_LBRACKET SubSelect SYM_RBRACKET
	{
	}
			
	| SYM_LBRACKET row_descriptor SYM_RBRACKET all_Op SYM_LBRACKET SubSelect SYM_RBRACKET
	{
	}
			
	| SYM_LBRACKET row_descriptor SYM_RBRACKET all_Op SYM_LBRACKET row_descriptor SYM_RBRACKET
	{
	}
			
	| SYM_LBRACKET row_descriptor SYM_RBRACKET KWD_OVERLAPS SYM_LBRACKET row_descriptor SYM_RBRACKET
	{
	}
			
	;

row_descriptor:  row_list SYM_COMMA a_expr
	{
	}
	;

row_list:  row_list SYM_COMMA a_expr
	{
	}
			
	| a_expr
	{
	}
			
	;

sub_type:  KWD_ANY
	{
	}
	| KWD_SOME
	{
	}
	| KWD_ALL
	{
	}
	;

all_Op:
	  SKWD_Op
	{
	}
	| MathOp
	{
	}
	;

MathOp:  OP_PLUS			
	{
	}
	| OP_MINUS			
	{
	}
	| SYM_STAR			
	{
	}
	| OP_DIV			
	{
	}
	| OP_MOD			
	{
	}
	| OP_XOR			
	{
	}
	| OP_OR			
	{
	}
	| OP_ST			
	{
	}
	| OP_LT			
	{
	}
	| OP_EQ			
	{
	}
	| OP_NOTEQ
	{
	}
	| SYM_DOLLAR
	{
	}
	;

/*
 * General expressions
 * This is the heart of the expression syntax.
 *
 * We have two expression types: a_expr is the unrestricted kind, and
 * b_expr is a subset that must be used in some places to avoid shift/reduce
 * conflicts.  For example, we can't do KWD_BETWEEN as "KWD_BETWEEN a_expr KWD_AND a_expr"
 * because that use of KWD_AND conflicts with KWD_AND as a boolean operator.  So,
 * b_expr is used in KWD_BETWEEN and we remove boolean keywords from b_expr.
 *
 * Note that SYM_LBRACKET a_expr SYM_RBRACKET is a b_expr, 
 * so an unrestricted expression can always be used by surrounding it with 
 * parens.
 *
 * c_expr is all the productions that are common to a_expr and b_expr;
 * it's factored out just to eliminate redundant coding.
 */
a_expr:  c_expr
	{
	}
			
	/*
	 * These operators must be called out explicitly in order to make use
	 * of yacc/bison's automatic operator-precedence handling.  All other
	 * operator names are handled by the generic productions using "Op",
	 * below; and all those operators will have the same precedence.
	 *
	 * If you add more explicitly-known operators, be sure to add them
	 * also to b_expr and to the MathOp list above.
	 */
	| OP_PLUS a_expr %prec UMINUS
	{
	}
			
	| OP_MINUS a_expr %prec UMINUS
	{
	}
			
	| OP_MOD a_expr
	{
	}
			
	| OP_XOR a_expr
	{
	}
			
	| OP_OR a_expr
	{
	}
			
	| SYM_COLON a_expr
	{
	}
			
	| SYM_SEMICOLON a_expr
	{
	}
			
	| a_expr OP_PLUS a_expr
	{
	}
			
	| a_expr OP_MINUS a_expr
	{
	}
			
	| a_expr SYM_STAR a_expr
	{
	}
			
	| a_expr OP_DIV a_expr
	{
	}
			
	| a_expr OP_MOD a_expr
	{
	}
			
	| a_expr OP_XOR a_expr
	{
	}
			
	| a_expr OP_OR a_expr
	{
	}
			
	| a_expr OP_ST a_expr
	{
	}
			
	| a_expr OP_LT a_expr
	{
	}
			
	| a_expr OP_EQ a_expr
	{
	}

	| a_expr OP_NOTEQ a_expr
	{
	}
			
	| a_expr SKWD_Op a_expr
	{
	}
			
	| SKWD_Op a_expr
	{
	}
			
	| a_expr KWD_AND a_expr
	{
	}
			
	| a_expr KWD_OR a_expr
	{
	}
			
	| KWD_NOT a_expr
	{
	}
			
	| a_expr KWD_LIKE a_expr
	{
	}
			
	| a_expr KWD_NOT KWD_LIKE a_expr
	{
	}
			
	| a_expr KWD_IS KWD_NULL_P
	{
	}
			
	| a_expr KWD_IS KWD_NOT KWD_NULL_P
	{
	}
			
	| a_expr KWD_IS KWD_TRUE_P
	{
	}
			
	| a_expr KWD_IS KWD_NOT KWD_FALSE_P
	{
	}
			
	| a_expr KWD_IS KWD_FALSE_P
	{
	}
			
	| a_expr KWD_IS KWD_NOT KWD_TRUE_P
	{
	}
			
	| a_expr KWD_BETWEEN b_expr KWD_AND b_expr
	{
	}
			
	| a_expr KWD_NOT KWD_BETWEEN b_expr KWD_AND b_expr
	{
	}

	| a_expr KWD_IN SubQueryBegin SubSelect SubQueryEnd
	{
	}
			
	| a_expr KWD_IN SYM_LBRACKET in_expr_nodes SYM_RBRACKET
	{
	}

	| a_expr KWD_NOT KWD_IN SubQueryBegin SubSelect SubQueryEnd
	{
	}

	| a_expr KWD_NOT KWD_IN SYM_LBRACKET in_expr_nodes SYM_RBRACKET
	{
	}
			
	| a_expr all_Op sub_type SubQueryBegin SubSelect SubQueryEnd
	{
	}

	| a_expr all_Op sub_type SYM_LBRACKET in_expr_nodes SYM_RBRACKET
	{
	}

	| row_expr
	{
	}

	;

/*
 * Restricted expressions
 *
 * b_expr is a subset of the complete expression syntax defined by a_expr.
 *
 * Presently, KWD_AND, KWD_NOT, KWD_IS, and KWD_IN are the a_expr keywords that would
 * cause trouble in the places where b_expr is used.  For simplicity, we
 * just eliminate all the boolean-keyword-operator productions from b_expr.
 */
b_expr:  c_expr
	{
	}
			
	| OP_PLUS b_expr %prec UMINUS
	{
	}
			
	| OP_MINUS b_expr %prec UMINUS
	{
	}
			
	| OP_MOD b_expr
	{
	}
			
	| OP_XOR b_expr
	{
	}
			
	| OP_OR b_expr
	{
	}
			
	| SYM_COLON b_expr
	{
	}
			
	| SYM_SEMICOLON b_expr
	{
	}
			
	| b_expr OP_PLUS b_expr
	{
	}
			
	| b_expr OP_MINUS b_expr
	{
	}
			
	| b_expr SYM_STAR b_expr
	{
	}
			
	| b_expr OP_DIV b_expr
	{
	}
			
	| b_expr OP_MOD b_expr
	{
	}
			
	| b_expr OP_XOR b_expr
	{
	}
			
	| b_expr OP_OR b_expr
	{
	}
			
	| b_expr OP_ST b_expr
	{
	}
			
	| b_expr OP_LT b_expr
	{
	}
			
	| b_expr OP_EQ b_expr
	{
	}

	| b_expr OP_NOTEQ b_expr
	{
	}
			
	| b_expr SKWD_Op b_expr
	{
	}
			
	| SKWD_Op b_expr
	{
	}
			
	;

/*
 * Productions that can be used in both a_expr and b_expr.
 *
 * Note: productions that refer recursively to a_expr or b_expr mostly
 * cannot appear here.  However, it's OK to refer to a_exprs that occur
 * inside parentheses, such as function arguments; that cannot introduce
 * ambiguity to the b_expr syntax.
 */
c_expr:  attr
	{
	}
			
	| ColId opt_indirection
	{
	}

	| ParamNo
	{
	}
			
	| AexprConst
	{
	}
			
	| SYM_LBRACKET a_expr SYM_RBRACKET
	{
	}
			
	| KWD_CAST SYM_LBRACKET a_expr KWD_AS Typename SYM_RBRACKET
	{
	}
			
	| case_expr
	{
	}
			
	| func_name SYM_LBRACKET SYM_RBRACKET
	{
	}
			
	| func_name SYM_LBRACKET expr_list SYM_RBRACKET
	{
	}
			
	| func_name SYM_LBRACKET KWD_ALL expr_list SYM_RBRACKET
	{
	}
			
	| func_name SYM_LBRACKET KWD_DISTINCT expr_list SYM_RBRACKET
	{
	}
			
	| func_name SYM_LBRACKET SYM_STAR SYM_RBRACKET
	{
	}
			
	| KWD_CURRENT_DATE
	{
	}
			
	| KWD_CURRENT_TIME
	{
	}
			
	| KWD_CURRENT_TIME SYM_LBRACKET Iconst SYM_RBRACKET
	{
	}
			
	| KWD_CURRENT_TIMESTAMP
	{
	}
			
	| KWD_CURRENT_TIMESTAMP SYM_LBRACKET Iconst SYM_RBRACKET
	{
	}
			
	| KWD_CURRENT_USER
	{
	}
			
	| KWD_SESSION_USER
	{
	}
			
	| KWD_USER
	{
	}
			
	| KWD_EXTRACT SYM_LBRACKET extract_list SYM_RBRACKET
	{
	}
			
	| KWD_POSITION SYM_LBRACKET position_list SYM_RBRACKET
	{
	}
			
	| KWD_SUBSTRING SYM_LBRACKET substr_list SYM_RBRACKET
	{
	}
			
	/* various trim expressions are defined in SQL92 */
	| KWD_TRIM SYM_LBRACKET KWD_BOTH trim_list SYM_RBRACKET
	{
	}
			
	| KWD_TRIM SYM_LBRACKET KWD_LEADING trim_list SYM_RBRACKET
	{
	}
			
	| KWD_TRIM SYM_LBRACKET KWD_TRAILING trim_list SYM_RBRACKET
	{
	}
			
	| KWD_TRIM SYM_LBRACKET trim_list SYM_RBRACKET
	{
	}
			
	| SubQueryBegin SubSelect SubQueryEnd
	{
	}
			
	| KWD_EXISTS SubQueryBegin SubSelect SubQueryEnd
	{
	}

	;

/*
 * Supporting nonterminals for expressions.
 */

opt_indirection:  SYM_LSQB a_expr SYM_RSQB opt_indirection
	{
	}
			
	| SYM_LSQB a_expr SYM_COLON a_expr SYM_RSQB opt_indirection
	{
	}
			
	| /*EMPTY*/
	{
	}
			
	;

expr_list: a_expr
	{
	}
			
	| expr_list SYM_COMMA a_expr
	{
	}
			
	| expr_list KWD_USING a_expr
	{
	}
	;

extract_list:  extract_arg KWD_FROM a_expr
	{
	}
			
	| /*EMPTY*/
	{
	}
			
	;

extract_arg:  datetime						
	{
	}
	| KWD_TIMEZONE_HOUR						
	{
	}
	| KWD_TIMEZONE_MINUTE					
	{
	}
	;

/* position_list uses b_expr not a_expr to avoid conflict with general KWD_IN */

position_list:  b_expr KWD_IN b_expr
			
	| /*EMPTY*/
	{
	}
			
	;

substr_list:  expr_list substr_from substr_for
	{
	}
			
	| /*EMPTY*/
	{
	}
			
	;

substr_from:  KWD_FROM expr_list
	{
	}
			
	| /*EMPTY*/
	{
	}
			
	;

substr_for: KWD_FOR expr_list
	{
	}
			
	| /*EMPTY*/
	{
	}
			
	;

trim_list: a_expr KWD_FROM expr_list
	{
	}
			
	| KWD_FROM expr_list
	{
	}
			
	| expr_list
	{
	}
			
	;

in_expr_nodes: a_expr
	{
	}
			
	| in_expr_nodes SYM_COMMA a_expr
	{
	}
			
	;

/* Case clause
 * Define SQL92-style case clause.
 * Allow all four forms described in the standard:
 * - Full specification
 *  KWD_CASE KWD_WHEN a = b KWD_THEN c ... KWD_ELSE d END
 * - Implicit argument
 *  KWD_CASE a KWD_WHEN b KWD_THEN c ... KWD_ELSE d END
 * - Conditional NULL
 *  KWD_NULLIF(x,y)
 *  same as KWD_CASE KWD_WHEN x = y KWD_THEN NULL KWD_ELSE x END
 * - Conditional substitution from list, use first non-null argument
 *  KWD_COALESCE(a,b,...)
 * same as KWD_CASE KWD_WHEN a KWD_IS KWD_NOT NULL KWD_THEN a KWD_WHEN b KWD_IS KWD_NOT NULL KWD_THEN b ... END
 */
case_expr: KWD_CASE case_arg when_clause_list case_default KWD_END_TRANS
	{
	}
			
	| KWD_NULLIF SYM_LBRACKET a_expr SYM_COMMA a_expr SYM_RBRACKET
	{
	}
			
	| KWD_COALESCE SYM_LBRACKET expr_list SYM_RBRACKET
	{
	}
			
	;

when_clause_list: when_clause_list when_clause
	{
	}
			
	| when_clause
	{
	}
			
	;

when_clause: KWD_WHEN a_expr KWD_THEN a_expr
	{
	}
			
	;

case_default: KWD_ELSE a_expr						
	{
	}
	| /*EMPTY*/								
	{
	}
	;

case_arg: a_expr
	{
	}
			
	| /*EMPTY*/
	{
	}
	;

attr: relation_name SYM_DOT attrs opt_indirection
	{
	}
			
	| ParamNo SYM_DOT attrs opt_indirection
	{
	}

/**
 * This rule is added to support Oracle outer join which likes following:
 * SELECT * FROM atab, btab WHERE atab.id(+) = btab.id
 *                                       ^^^
 * or
 * SELECT * FROM atab, btab WHERE atab.id = btab.id(+)
 */
	| attr oracle_outer
	{
	}
	;

oracle_outer: SYM_LBRACKET OP_PLUS SYM_RBRACKET
	{
	}
	;

attrs: attr_name
	{
	}
			
	| attrs SYM_DOT attr_name
	{
	}
			
	| attrs SYM_DOT SYM_STAR
	{
	}
	;


/*****************************************************************************
 *
 *	target lists
 *
 *****************************************************************************/

/* Target lists as found in KWD_SELECT ... and KWD_INSERT KWD_VALUES ( ... ) */

target_list: target_list SYM_COMMA target_el
	{
	}
	| target_el
	{
	}
	;

/* KWD_AS is not optional because shift/red conflict with unary ops */
target_el: a_expr KWD_AS ColLabel
	{
	}

/**
 * This rule is added to support using blank instead of AS to declare 
 * column alias.
 * SELECT cola cola_al, colb colb_al FROM atable;
 */
	| a_expr SKWD_IDENT
	{
	}

	| a_expr
	{
	}
			
	| relation_name SYM_DOT SYM_STAR
	{
	}

	| SYM_STAR
	{
	}
	;

/* Target list as found in KWD_UPDATE table KWD_SET ... */

update_target_list: update_target_list SYM_COMMA update_target_el
	{
	}
			
	| update_target_el
	{
	}
			
	;

update_target_el: ColId opt_indirection OP_EQ a_expr
	{
	}

	| ColId SYM_DOT ColId OP_EQ a_expr
	{
	}
			
	;

/*****************************************************************************
 *
 *	Names and constants
 *
 *****************************************************************************/

relation_name: SpecialRuleRelation
	{
	}
			
	| ColId
	{
	}
			
	;

attr_name: ColId
	{
	}			
	;

/* Functions
 * Include date/time keywords as SQL92 extension.
 * Include TYPE as a SQL92 unreserved keyword.
 */
name: ColId
	{
	}
	;
func_name: ColId
	{
	}
/**
 * This rule is added to support the function LEFT() of MS SQLServer 2K.
 * For example:
 * SELECT LEFT(cola, 4) AS left_4_cola
 *        ^^^^^^^^^^^^^
 * FROM atable
 */
	| KWD_LEFT
	{
	}
	;

/* Constants
 * Include TRUE/FALSE for SQL3 support.
 */
AexprConst:  Iconst
	{
	}
			
	| SKWD_FCONST
	{
	}
			
	| Sconst
	{
	}
			
	/* this rule formerly used Typename, but that causes reduce conflicts
	 * with subscripted column names ...
	 */
	| SimpleTypename Sconst
	{
	}
			
	| KWD_TRUE_P
	{
	}
			
	| KWD_FALSE_P
	{
	}
			
	| KWD_NULL_P
	{
	}
			
	;

ParamNo:  SKWD_PARAM opt_indirection
	{
	}
			
	;

Iconst:  SKWD_ICONST
	{
	}
	;

Sconst:  SKWD_SCONST
	{
	}
	;

/* Column identifier
 * Include date/time keywords as SQL92 extension.
 */
ColId:  SKWD_IDENT
	{
	}
	| TokenId						
	{
	}
	| datetime						
	{
	}
	| KWD_INTERVAL						
	{
	}
	| KWD_TIME							
	{
	}
	| KWD_TIMESTAMP						
	{
	}
	| KWD_TYPE_P						
	{
	}
	;

/* Parser tokens to be used as identifiers.
 * Tokens involving data types should appear in ColId only,
 * since they will conflict with real TypeName productions.
 */
TokenId:  KWD_ABSOLUTE						
	{
	}
	| KWD_ACTION						
	{
	}
	| KWD_ADD							
	{
	}
	| KWD_ALTER							
	{
	}
	| KWD_BEGIN_TRANS					
	{
	}
	| KWD_BY							
	{
	}
	| KWD_CASCADE						
	{
	}
	| KWD_CLOSE							
	{
	}
	| KWD_COMMIT						
	{
	}
	| KWD_COMMITTED						
	{
	}
	| KWD_CONSTRAINTS					
	{
	}
	| KWD_CREATE						
	{
	}
	| KWD_CURSOR						
	{
	}
	| KWD_DECLARE						
	{
	}
	| KWD_DEFERRED						
	{
	}
	| KWD_DELETE						
	{
	}
	| KWD_DOUBLE						
	{
	}
	| KWD_DROP							
	{
	}
	| KWD_EXECUTE						
	{
	}
	| KWD_FETCH							
	{
	}
	| KWD_GRANT							
	{
	}
	| KWD_IMMEDIATE						
	{
	}
	| KWD_INSENSITIVE					
	{
	}
	| KWD_INSERT						
	{
	}
	| KWD_ISOLATION						
	{
	}
	| KWD_KEY							
	{
	}
	| KWD_LANGUAGE						
	{
	}
	| KWD_LEVEL							
	{
	}
	| KWD_MATCH							
	{
	}
	| KWD_NAMES							
	{
	}
	| KWD_NATIONAL						
	{
	}
	| KWD_NEXT							
	{
	}
	| KWD_NO							
	{
	}
	| KWD_OF							
	{
	}
	| KWD_ONLY							
	{
	}
	| KWD_OPTION						
	{
	}
	| KWD_PARTIAL						
	{
	}
	| KWD_PENDANT						
	{
	}
	| KWD_PRIOR							
	{
	}
	| KWD_PRIVILEGES					
	{
	}
	| KWD_READ							
	{
	}
	| KWD_RELATIVE						
	{
	}
	| KWD_RESTRICT						
	{
	}
	| KWD_REVOKE						
	{
	}
	| KWD_ROLLBACK						
	{
	}
	| KWD_SCROLL						
	{
	}
	| KWD_SERIALIZABLE					
	{
	}
	| KWD_SET							
	{
	}
	| KWD_TEMPORARY						
	{
	}
	| KWD_TIMEZONE_HOUR					
	{
	}
	| KWD_TIMEZONE_MINUTE				
	{
	}
	| KWD_TRIGGER						
	{
	}
	| KWD_UPDATE						
	{
	}
	| KWD_VALUES						
	{
	}
	| KWD_VARYING						
	{
	}
	| KWD_VIEW							
	{
	}
	| KWD_WITH							
	{
	}
	| KWD_WORK							
	{
	}
	| KWD_ZONE							
	{
	}
	;

/* Column label
 * Allowed labels in "KWD_AS" clauses.
 */
ColLabel:  ColId						
	{
	}
	| KWD_ALL							
	{
	}
	| KWD_ANY							
	{
	}
	| KWD_ASC							
	{
	}
	| KWD_BETWEEN						
	{
	}
	| KWD_BOTH							
	{
	}
	| KWD_CASE							
	{
	}
	| KWD_CAST							
	{
	}
	| KWD_CHAR							
	{
	}
	| KWD_CHARACTER						
	{
	}
	| KWD_CHECK							
	{
	}
	| KWD_COALESCE						
	{
	}
	| KWD_COLLATE						
	{
	}
	| KWD_COLUMN						
	{
	}
	| KWD_CONSTRAINT					
	{
	}
	| KWD_CROSS							
	{
	}
	| KWD_CURRENT						
	{
	}
	| KWD_CURRENT_DATE					
	{
	}
	| KWD_CURRENT_TIME					
	{
	}
	| KWD_CURRENT_TIMESTAMP				
	{
	}
	| KWD_CURRENT_USER					
	{
	}
	| KWD_DEC							
	{
	}
	| KWD_DECIMAL						
	{
	}
	| KWD_DEFAULT						
	{
	}
	| KWD_DEFERRABLE					
	{
	}
	| KWD_DESC							
	{
	}
	| KWD_DISTINCT						
	{
	}
	| KWD_ELSE							
	{
	}
	| KWD_END_TRANS						
	{
	}
	| KWD_EXCEPT						
	{
	}
	| KWD_EXISTS						
	{
	}
	| KWD_EXTRACT						
	{
	}
	| KWD_FALSE_P						
	{
	}
	| KWD_FLOAT							
	{
	}
	| KWD_FOR							
	{
	}
	| KWD_FOREIGN						
	{
	}
	| KWD_FROM							
	{
	}
	| KWD_FULL							
	{
	}
	| KWD_GLOBAL						
	{
	}
	| KWD_GROUP							
	{
	}
	| KWD_HAVING						
	{
	}
	| KWD_INITIALLY						
	{
	}
	| KWD_IN							
	{
	}
	| KWD_INNER_P						
	{
	}
	| KWD_INTERSECT						
	{
	}
	| KWD_INTO							
	{
	}
	| KWD_IS							
	{
	}
	| KWD_JOIN							
	{
	}
	| KWD_LEADING						
	{
	}
	| KWD_LIKE							
	{
	}
	| KWD_LOCAL							
	{
	}
	| KWD_NATURAL						
	{
	}
	| KWD_NCHAR							
	{
	}
	| KWD_NOT							
	{
	}
	| KWD_NULLIF						
	{
	}
	| KWD_NULL_P						
	{
	}
	| KWD_NUMERIC						
	{
	}
	| KWD_ON							
	{
	}
	| KWD_OR							
	{
	}
	| KWD_ORDER							
	{
	}
	| KWD_OUTER_P						
	{
	}
	| KWD_OVERLAPS						
	{
	}
	| KWD_POSITION						
	{
	}
	| KWD_PRECISION						
	{
	}
	| KWD_PRIMARY						
	{
	}
	| KWD_PROCEDURE						
	{
	}
	| KWD_PUBLIC						
	{
	}
	| KWD_REFERENCES					
	{
	}
	| KWD_LEFT
	{
	}
	| KWD_RIGHT							
	{
	}
	| KWD_SELECT						
	{
	}
	| KWD_SESSION_USER					
	{
	}
	| KWD_SOME							
	{
	}
	| KWD_SUBSTRING						
	{
	}
	| KWD_TABLE							
	{
	}
	| KWD_THEN							
	{
	}
	| KWD_TO							
	{
	}
	| KWD_TRANSACTION					
	{
	}
	| KWD_TRIM							
	{
	}
	| KWD_TRUE_P						
	{
	}
	| KWD_UNIQUE						
	{
	}
	| KWD_USER							
	{
	}
	| KWD_USING							
	{
	}
	| KWD_VARCHAR						
	{
	}
	| KWD_WHEN							
	{
	}
	| KWD_WHERE							
	{
	}
	;

SpecialRuleRelation:  KWD_CURRENT
	{
	}
	;
%%

	private SQLLexer lexer;

	private int yylex() {
		int yyl_return = -1;
		try {
			yylval = new SQLParserVal("");
			yyl_return = lexer.yylex();
		} catch (IOException e) {
			System.err.println("IO error :" + e);
		}

		return yyl_return;
	}

	public void yyerror(String errMsg) {
		System.err.println("ÇrÇpÇkï∂ñ@ÉGÉâÅ[ÇÕ " + 
			lexer.getyyline() + " çsÇÃ " + "\"" + lexer.yytext() + "\"" +
			" ÇÃãﬂÇ≠Ç≈î≠ê∂ÇµÇ‹ÇµÇΩÅB" );
	}

	public SQLParser(java.io.Reader reader) {
		this();
		lexer = new SQLLexer(reader, this);
	}

	public void setReader(java.io.Reader reader) {
		lexer = new SQLLexer(reader, this);
	}

	public static void main(String[] args) {
		byte[] inputBuffer = new byte[1024];
		byte[] sqlBytes;
		int nread, i;
		SQLParser parser = null;

		while (true) {
			try {
				System.out.print("please input SQL statement:");
				nread = System.in.read(inputBuffer, 0, 1024);

				sqlBytes = new byte[nread];
				for (i = 0; i < nread; ++i)
					sqlBytes[i] = inputBuffer[i];

				InputStreamReader isReader = new InputStreamReader(
					new ByteArrayInputStream(sqlBytes), "ISO-8859-1"); 

				if (parser == null) {
					parser = new SQLParser(isReader);
				} else {
					parser.setReader(isReader);
				}

				if (parser.yyparse() == 0) {
					System.out.println("accept");
				}
			} catch (UnsupportedEncodingException e) {
				System.out.println(e);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}
