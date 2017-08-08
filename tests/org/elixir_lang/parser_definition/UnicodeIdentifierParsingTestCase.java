package org.elixir_lang.parser_definition;

public class UnicodeIdentifierParsingTestCase extends ParsingTestCase {
    public void testElixirTestElixirExceptionTest() {
        assertParsedAndQuotedCorrectly();
    }

    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "/unicode_identifier_parsing_test_case";
    }
}
