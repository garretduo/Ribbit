# Ribbit
                        ___  _  _    _    _    _   
                        | . \<_>| |_ | |_ <_> _| |_ 
                        |   /| || . \| . \| |  | |  
                        |_\_\|_||___/|___/|_|  |_|  
                              by Garret Duo

README
    Ribbit is a dynamically typed programming language that I made for my AP
    Comp. Sci. A class. It's reasonably fleshed out, except without functions 
    or classes or anything you'd expect from a complete, modern-day language.
    Not to worry though, I might add that in the future. 
    
    For now, it supports variables, basic expressions, if and else 
    conditionals, while and for loops, and a couple other fundamental features. 
    Everything will be fully explained below!

MAKING A FILE
    Ribbit supports .txt files, so no fancy extensions as of yet. To run a .txt
    file of your choice, you must first install the Java JRE, as the current 
    (and only) interpreter for Ribbit is written in Java. Once installed, run 
    the command 'java -jar Ribbit.jar [filename].txt' in your terminal. Your
    program will then execute!

RESERVED KEYWORDS
    Before we begin, here's a quick reference to all reserved keywords:
    if
    else
    and
    or
    true
    false
    disp
    input
    return
    let
    for
    while
    then
    do

    Don't try and declare variable names with those keywords!

VARIABLES 
    Like mentioned above, all variables in Ribbit are dynamically-typed. This
    means that you do not have to explicitly declare whether or not a variable 
    is an integer or string, for example. Ribbit supports the following 
    variable types:

    Integer (Java equivalent: long)
    String (Java equivalent: String)
    Boolean (Java equivalent: boolean)

    Unfortunately, I was too lazy to add in support for floating-point numbers, 
    but maybe in the future.

    DECLARING VARIABLES
        To declare a variable, use the keyword 'let', followed by a variable name.

        Example:
        let x

        (declares a variable with name x, with no initial value)

        You can also initialize a variable right off the bat to a value.

        Example:
        let x = 5

        (declares a variable with name x, with an initial INTEGER value of 5)

    STRINGS
        Because this is a dynamically-typed language, to initialize a string you
        must surround the string value with quotation marks.

        Example:
        let x = "Hello!"

        (declares a variable with name x, with an initial STRING value of "Hello!")

    INTEGERS
        To initialize an integer, you simply set the variable equal to an integer
        number, without quotation marks.

        Example:
        let x = 5

        (variable x is now an INTEGER)

        Example:
        let x = "5"

        (variable x is now a STRING)
    
    BOOLEANS
        This process can also be repeated for our third data type, booleans.

        Example:
        let x = true

        (declares a variable with name x, with an initial BOOLEAN value of 'true')

    DYNAMICALLY TYPED VARIABLES
        Notice that I've explicitly mentioned the keyword 'initial'. That's because
        Ribbit allows you to change variable values (there is currently no support
        for constants, so ALL variables can change values). 
        
        What's more, because the language is dynamically typed, integers can become 
        strings, strings into booleans, and so on. It's pretty neat.

        Example:
        let x = 5
        x = "Hi"

        (variable x is now a string!)

        You can even re-use variable names.

        Example:
        let x = 5
        let x = 7

        (this is valid, although it might not be the best design practice)

OPERATORS
    Ribbit supports most of the operator types you'd need for programming. They
    are:

    math operators
    comparison operators
    conditional operators
    unary operators

    MATH OPERATORS
        Ribbit supports all of the basic math operations you might see in other
        languages. They are:

        ADDITION (+)
        SUBTRACTION (-)
        MULTIPLICATION (*)
        DIVISION (/)
        MODULUS (%)

        Like most languages, to use these you need a left and right operand.

        Example:
        let x = 5 + 4

        (5 + 4 evalulates to 9)

        However, Ribbit does NOT support math assignment operators commonly seen
        in other languages.

        Example:
        let x = 5
        x += 1

        (this is invalid)

        There are, however, alternative solutions that do work with Ribbit.

        Example:
        let x = 5
        x = x + 1

        (this is valid)

        Maybe I'll add math assignment in the future. For now, you'll have to work
        with this. Sorry.

        One more cool thing. You can also concatenate strings together with the
        '+' operator. If you concatenate a string with an integer, the integer
        will be treated as a string.

        Example:
        let x = "Hello, " + "World!"

        (x has a value of "Hello, world!")

        Example:
        let x = 5 + "Hello!"

        (x has a value of "5Hello!")

    COMPARISON OPERATIONS
        Ribbit supports all of the comparison you'd see in Java, with a few slight
        changes. They are:

        EQUAL TO (==)
        NOT EQUAL TO (!=)
        GREATER THAN (>)
        GREATER THAN OR EQUAL TO (>=) 
        LESS THAN (<)
        LESS THAN OR EQUAL TO (<=)

        Example:
        let x = 5
        let y = (x == 5)
        (y has a value of 'true')

        What's really cool is that, unlike Java, strings can be compared with
        the '==' operator as well. No more ".equals()" non-sense!

        Example:
        let x = "Hello!"
        let y = "Hello!"
        let z = (x == y)
        (z has a value of 'true')

    CONDITIONAL OPERATORS
        Ribbit supports two important conditional operators. They are:

        AND (and)
        OR (or)

        To use them, simply put them in between two boolean expressions.

        Example:
        let x = 1
        let y = 5
        if x == 1 and y == 5 do
        (we haven't covered if statements yet, but you can see how 'and' and 'or' 
        work)

    UNARY OPERATORS
        Ribbit supports two unary operators. They are:

        MINUS (-)
        NOT (!)

        Applying '-' to an integer turns it to its negative compliment. Applying 
        '!' to a boolean expression turns it into its logical opposite.

        Example:
        let x = 5
        let y = -x
        (y has a value of -5)

        let z = true
        let w = !z
        (w has a value of false)

CONTROL FLOW
    Like any good language, Ribbit does indeed support basic control flow
    structures. These include:

    if/else statements
    while loops
    for loops
    blocks

    IF/ELSE
        To use an if statement, use the 'if' keyword, followed by a condition.
        After the condition, use a 'then' keyword before the statement.

        Example:
        let x = 5
        if x == 5 then
            disp "Yes!"

        (outputs "Yes!" to the console)

        Multi-line blocks are supported with the block characters mentioned
        later.

        Else statements are also supported with the 'else' keyword.

        Example:
        let x = 5
        if x == 4 then
            disp "Yes!"
        else
            disp "No... :("

        (outputs "No... :(" to the console)

    WHILE LOOPS
        To create a while loop, use the keyword 'while' followed by a condition
        of some sort. After the condition, use a 'do' keyword and then your
        statement to be run.

        Example:
        let x = 0
        while x < 10 do:
            disp x
            x = x + 1
        ;

        (yes, this shows off multi-line blocks; I'll explain that shortly)
        (displays integers from 0 to 9 to console)

    FOR LOOPS
        To create a for loop, use the keyword 'for' followed by parentheses in
        this format:

        for(initializer, condition, increment) do

        Example:
        for(let i = 0, i < 10, i = i + 1) do
            disp i

        (displays integers from 0 to 9 to console)

        Strictly speaking, you can omit the initializer, condition, and
        increment as you see fit. If you omit the condition, it will by be set
        to 'true' by default.

    BLOCKS
        To create a block, use the ':' character to open the block and the ';'
        keyword to close it.

        Example:
        :
            let x = 5
        ;

        Blocks also support variable scoping, so a variable in an inner block
        cannot be accessed in an outer block (but not vice versa).

        Example:
        let x = 1
        let y = 2
        :
            let y = 3
            disp x
        ;
        disp y

        (displays 
        "1
        2" to console)

        You can also nest blocks.

        Example:

        for (let i = 0, i < 2, i = i + 1) do:
            for (let j = 0, j < 2, j = j + 1) do:
                disp j
            ;
        ;

        (displays
        "0
        1
        0
        1" to console)

INPUT/OUTPUT
    Ribbit supports basic console I/O, meaning you can display text to a
    console and take in input from a keyword. However, it does not support any
    fancy file read/write, for example.

    INPUT
        Although most languages make input a library function, I have opted to
        build it into the language itself since Ribbit does not (yet) support
        functions. To gather input, simply use the 'input' keyword, which
        prompts the user for a console input and returns that value.

        Example:
        input

        (takes in an input and returns it)

        But wait! How is this useful you might ask? Because 'input' returns a
        value, you can use store its value into a variable.

        Example:
        let x = input

        (takes in an input an stores it in x)

        Alternatively, you could use an input and discard it right away without
        storing it, similar to function return values in Java.

    OUTPUT
        Similar to input, output is built in to the language, rather than made
        into a library function. To display text to the console, simply use the
        'disp' keyword, followed by either a string, an integer, a variable, or
        an expression.

        Example:
        disp 1

        (displays "1" to console)

        Example:
        disp "Hello, world!"

        (displays "Hello, world!" to console)

        Example:
        let x = "Hello!"
        disp x

        (displays "Hello!" to console)

        Example:
        disp (5 == 5)

        (displays "true" to console)

LISTS
    Ribbit currently supports one data structure -- lists. They are similar to
    Python lists in that they can hold objects of different data types. For
    example, this is a valid list:

    [1, "Hello, ", 5, true, "World!"]

    CREATING A LIST
        To declare a list, use the '[' and ']' characters, with an optional size
        argument between the brackets.

        Example:
        let x = []

        (declares and initializes variable x to an empty list)

        Example:
        let x = [5]

        (declares and initializes variable x to a list with an initial size of 5)

        By default, all undeclared values of a list are set to 0.

    ACCESSING A LIST
        To access a specific index of a list, use the '{' and '}' characters
        after the list identifier, with an index in between the braces.

        Example:
        let x = [2]
        x{0} = "Hi!"
        disp x{0}

        (sets index 0 of list x to the value "Hi!", and then displays "Hi!" to
        console) 

        To print out all values of a list, you can simply pass in the list
        identifier to the "disp" keyword.

        Example:
        let x = [1]
        x{0} = 1
        disp x

        (displays "[1]" to console)

        Lists also support variable indexes, meaning this is valid:

        let x = [3]
        x{0} = "Jack"
        x{1} = "Jill"
        x{3} = "John"
        for(let i = 0, i < 3, i = i + 1) do
            disp x{i}

        (displays
        "Jack
        Jill
        John" to console)

    ADDING TO A LIST
        To add to a list, use the standard "identifier{index}" syntax, but
        use an index greater than the current size.

        Example:

        let x = [1]
        x{0} = "Hello, "
        disp x
        x{1} = "World!"
        disp x

        (displays
        "["Hello, ]
        ["Hello, ", "World!"] to console)

        If the specified index is more than 1 greater than the current maximum
        index, the spaces in between will be initialized with default values of
        0.

    APPENDING TO A LIST
        To append to a list, use the standard "identifier{index}" syntax, but
        use a '+' character in place of the index.

        Example:
        let x = [5]
        x{+} = "Hi!"
        disp x

        (displays
        "[0, 0, 0, 0, 0, "Hi!"] to console)

    DELETING ELEMENTS IN A LIST
        To delete elements from a list, use the standard "identifier{index}"
        syntax, but use a '-' character in place of the index. By default, this
        deletes the last element in a list.

        Example:
        let x = [5]
        x{-}
        disp x

        (displays
        "[0, 0, 0, 0]" to console)

        To delete an element at a specific index, add an assignment operator,
        with the index to be deleted on the right-hand side.

        Example:
        let x = [2]
        x{0} = "Hi!"
        x{1} = "Bye!"
        x{-} = 0
        disp x

        (displays
        "["Bye!"]" to console)

    LIST LENGTH
        To access the list length, use the '$' character in place of the index.

        Example:
        let x = [5]
        disp x{$}

        (displays "5" to console);

COMMENTS
    Ribbit supports both single and multi-line comments.

    SINGLE-LINE COMMENTS   
        To do a single-line comment, use the character '#' followed by your
        comment. Comments will automatically end at the next line.

        Example:
        let x = 5 # this is a variable

        (all text from '#' to the end of the line is not run)

    MULTI-LINE COMMENTS
        To do a multi-line comment, use the character '@' followed by your
        comment. Comments will only end at the next occurrence of '@'.

        Example:
        let x = 5
        @ I can type forever and ever.
        Even onto the next line!
        This is getting old... @

        (all text from '@' to '@' is not run)

ERROR CHECKING
    Ribbit supports basic error checking and can detect both compile and
    runtime errors. However, it is by no means exhaustive and there are
    certainly corner cases I haven't added in yet. Most of the obvious errors,
    such as division by 0 or referencing an undeclared variable are implemented,
    however.

FORMATTING
    The compiler will certainly allow you to write Ribbit code without
    following some formatting guidelines, but these are good readability
    guidelines to follow.
    
    NEW LINE USAGE
        You could very well do this:

        let x = 6 let y = 4 disp x + y
        (displays "10" to console)

        However, it's not readable at all. Instead, place new statements on new
        lines:

        let x = 6
        let y = 4
        disp x + y
        (much better!)

    INDENTATION
        Indent for new blocks. Like in Java, it's not required. But it will be
        much more readable.

    BLOCKS
        Strictly speaking, you can create blocks like this:

        :let x = 5;

        For single-line blocks, that might be ideal. But for multi-line blocks,
        an "Egyptian Brackets" or "K&R" style should be used:

        Example:
        let x = 5
        if x > 4 do:
            disp x
            disp "What!"
        ;

        (Egyptian brackets)

        Example:
        let x = 5
        if x > 4 do
        :
            disp x
            disp "What!"
        ;

        (K&R)

CONCLUSION
    For what started as a relatively unambitious project, I really enjoyed
    fleshing out "Ribbit" into a somewhat competent scripting language. If and
    when I have more time, I'll probably continue updating this language to add
    support for functions, classes, and maybe basic object-oriented concepts. 
    
    A special thanks to the book "Crafting Interpreters" by Robert Nystrom, as 
    it taught me a lot and gave me a good direction and foundation to build 
    this interpreter. And a special thanks to my AP Comp. Sci. A. teacher, Mr.
    Jan, for doing an amazing job teaching and getting me so interested in 
    Computer Science. I loved all the in-class projects, even Mowing.
