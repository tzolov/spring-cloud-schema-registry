/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fasterxml.jackson.dataformat.avro.schema;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import org.apache.avro.Schema;

/**
 * @author Christian Tzolov
 */
public class PatchAvroSchemaGenerator extends VisitorFormatWrapperImpl {

	public PatchAvroSchemaGenerator(DefinedSchemas schemas, SerializerProvider p) {
		super(schemas, p);
	}

	public PatchAvroSchemaGenerator() {
		// NOTE: null is fine here, as provider links itself after construction
		super(new DefinedSchemas(), null);
	}

	public AvroSchema getGeneratedSchema() {
		return new AvroSchema(getAvroSchema());
	}

	@Override
	public JsonObjectFormatVisitor expectObjectFormat(JavaType type) {

		Schema s = _schemas.findSchema(type);
		if (s != null) {
			_valueSchema = s;
			return null;
		}
		PatchRecordVisitor v = new PatchRecordVisitor(_provider, type, _schemas);
		_builder = v;
		return v;
	}
}
