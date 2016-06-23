// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ChatMessage.proto

package proto;

public final class ChatMessageProto {
    private static final com.google.protobuf.Descriptors.Descriptor
            internal_static_proto_ChatMessage_descriptor;
    private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internal_static_proto_ChatMessage_fieldAccessorTable;
    private static com.google.protobuf.Descriptors.FileDescriptor
            descriptor;

    static {
        java.lang.String[] descriptorData = {
                "\n\021ChatMessage.proto\022\005proto\032\016Entities.pro" +
                        "to\"I\n\013ChatMessage\022\031\n\004user\030\001 \002(\0132\013.proto." +
                        "User\022\017\n\007message\030\002 \002(\t\022\016\n\006chatId\030\003 \002(\005B\031\n" +
                        "\005protoB\020ChatMessageProto"
        };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
                new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
                    public com.google.protobuf.ExtensionRegistry assignDescriptors(
                            com.google.protobuf.Descriptors.FileDescriptor root) {
                        descriptor = root;
                        return null;
                    }
                };
        com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{
                                proto.EntitiesProto.getDescriptor(),
                        }, assigner);
        internal_static_proto_ChatMessage_descriptor =
                getDescriptor().getMessageTypes().get(0);
        internal_static_proto_ChatMessage_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                internal_static_proto_ChatMessage_descriptor,
                new java.lang.String[]{"User", "Message", "ChatId",});
        proto.EntitiesProto.getDescriptor();
    }

    private ChatMessageProto() {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistry registry) {
    }

    public static com.google.protobuf.Descriptors.FileDescriptor
    getDescriptor() {
        return descriptor;
    }

    public interface ChatMessageOrBuilder extends
            // @@protoc_insertion_point(interface_extends:proto.ChatMessage)
            com.google.protobuf.MessageOrBuilder {

        /**
         * <code>required .proto.User user = 1;</code>
         */
        boolean hasUser();

        /**
         * <code>required .proto.User user = 1;</code>
         */
        proto.EntitiesProto.User getUser();

        /**
         * <code>required .proto.User user = 1;</code>
         */
        proto.EntitiesProto.UserOrBuilder getUserOrBuilder();

        /**
         * <code>required string message = 2;</code>
         */
        boolean hasMessage();

        /**
         * <code>required string message = 2;</code>
         */
        java.lang.String getMessage();

        /**
         * <code>required string message = 2;</code>
         */
        com.google.protobuf.ByteString
        getMessageBytes();

        /**
         * <code>required int32 chatId = 3;</code>
         */
        boolean hasChatId();

        /**
         * <code>required int32 chatId = 3;</code>
         */
        int getChatId();
    }

    /**
     * Protobuf type {@code proto.ChatMessage}
     */
    public static final class ChatMessage extends
            com.google.protobuf.GeneratedMessage implements
            // @@protoc_insertion_point(message_implements:proto.ChatMessage)
            ChatMessageOrBuilder {
        public static final int USER_FIELD_NUMBER = 1;
        public static final int MESSAGE_FIELD_NUMBER = 2;
        public static final int CHATID_FIELD_NUMBER = 3;
        private static final ChatMessage defaultInstance;
        private static final long serialVersionUID = 0L;
        public static com.google.protobuf.Parser<ChatMessage> PARSER =
                new com.google.protobuf.AbstractParser<ChatMessage>() {
                    public ChatMessage parsePartialFrom(
                            com.google.protobuf.CodedInputStream input,
                            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws com.google.protobuf.InvalidProtocolBufferException {
                        return new ChatMessage(input, extensionRegistry);
                    }
                };

        static {
            defaultInstance = new ChatMessage(true);
            defaultInstance.initFields();
        }

        private final com.google.protobuf.UnknownFieldSet unknownFields;
        private int bitField0_;
        private proto.EntitiesProto.User user_;
        private java.lang.Object message_;
        private int chatId_;
        private byte memoizedIsInitialized = -1;
        private int memoizedSerializedSize = -1;
        // Use ChatMessage.newBuilder() to construct.
        private ChatMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private ChatMessage(boolean noInit) {
            this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
        }

        private ChatMessage(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            initFields();
            int mutable_bitField0_ = 0;
            com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                    com.google.protobuf.UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        default: {
                            if (!parseUnknownField(input, unknownFields,
                                    extensionRegistry, tag)) {
                                done = true;
                            }
                            break;
                        }
                        case 10: {
                            proto.EntitiesProto.User.Builder subBuilder = null;
                            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                                subBuilder = user_.toBuilder();
                            }
                            user_ = input.readMessage(proto.EntitiesProto.User.PARSER, extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(user_);
                                user_ = subBuilder.buildPartial();
                            }
                            bitField0_ |= 0x00000001;
                            break;
                        }
                        case 18: {
                            com.google.protobuf.ByteString bs = input.readBytes();
                            bitField0_ |= 0x00000002;
                            message_ = bs;
                            break;
                        }
                        case 24: {
                            bitField0_ |= 0x00000004;
                            chatId_ = input.readInt32();
                            break;
                        }
                    }
                }
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(this);
            } catch (java.io.IOException e) {
                throw new com.google.protobuf.InvalidProtocolBufferException(
                        e.getMessage()).setUnfinishedMessage(this);
            } finally {
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static ChatMessage getDefaultInstance() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return proto.ChatMessageProto.internal_static_proto_ChatMessage_descriptor;
        }

        public static proto.ChatMessageProto.ChatMessage parseFrom(
                com.google.protobuf.ByteString data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static proto.ChatMessageProto.ChatMessage parseFrom(
                com.google.protobuf.ByteString data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static proto.ChatMessageProto.ChatMessage parseFrom(byte[] data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static proto.ChatMessageProto.ChatMessage parseFrom(
                byte[] data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static proto.ChatMessageProto.ChatMessage parseFrom(java.io.InputStream input)
                throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static proto.ChatMessageProto.ChatMessage parseFrom(
                java.io.InputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static proto.ChatMessageProto.ChatMessage parseDelimitedFrom(java.io.InputStream input)
                throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static proto.ChatMessageProto.ChatMessage parseDelimitedFrom(
                java.io.InputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static proto.ChatMessageProto.ChatMessage parseFrom(
                com.google.protobuf.CodedInputStream input)
                throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static proto.ChatMessageProto.ChatMessage parseFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(proto.ChatMessageProto.ChatMessage prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public ChatMessage getDefaultInstanceForType() {
            return defaultInstance;
        }

        @java.lang.Override
        public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
            return this.unknownFields;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return proto.ChatMessageProto.internal_static_proto_ChatMessage_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            proto.ChatMessageProto.ChatMessage.class, proto.ChatMessageProto.ChatMessage.Builder.class);
        }

        @java.lang.Override
        public com.google.protobuf.Parser<ChatMessage> getParserForType() {
            return PARSER;
        }

        /**
         * <code>required .proto.User user = 1;</code>
         */
        public boolean hasUser() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        /**
         * <code>required .proto.User user = 1;</code>
         */
        public proto.EntitiesProto.User getUser() {
            return user_;
        }

        /**
         * <code>required .proto.User user = 1;</code>
         */
        public proto.EntitiesProto.UserOrBuilder getUserOrBuilder() {
            return user_;
        }

        /**
         * <code>required string message = 2;</code>
         */
        public boolean hasMessage() {
            return ((bitField0_ & 0x00000002) == 0x00000002);
        }

        /**
         * <code>required string message = 2;</code>
         */
        public java.lang.String getMessage() {
            java.lang.Object ref = message_;
            if (ref instanceof java.lang.String) {
                return (java.lang.String) ref;
            } else {
                com.google.protobuf.ByteString bs =
                        (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    message_ = s;
                }
                return s;
            }
        }

        /**
         * <code>required string message = 2;</code>
         */
        public com.google.protobuf.ByteString
        getMessageBytes() {
            java.lang.Object ref = message_;
            if (ref instanceof java.lang.String) {
                com.google.protobuf.ByteString b =
                        com.google.protobuf.ByteString.copyFromUtf8(
                                (java.lang.String) ref);
                message_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        /**
         * <code>required int32 chatId = 3;</code>
         */
        public boolean hasChatId() {
            return ((bitField0_ & 0x00000004) == 0x00000004);
        }

        /**
         * <code>required int32 chatId = 3;</code>
         */
        public int getChatId() {
            return chatId_;
        }

        private void initFields() {
            user_ = proto.EntitiesProto.User.getDefaultInstance();
            message_ = "";
            chatId_ = 0;
        }

        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized == 1) return true;
            if (isInitialized == 0) return false;

            if (!hasUser()) {
                memoizedIsInitialized = 0;
                return false;
            }
            if (!hasMessage()) {
                memoizedIsInitialized = 0;
                return false;
            }
            if (!hasChatId()) {
                memoizedIsInitialized = 0;
                return false;
            }
            if (!getUser().isInitialized()) {
                memoizedIsInitialized = 0;
                return false;
            }
            memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output)
                throws java.io.IOException {
            getSerializedSize();
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                output.writeMessage(1, user_);
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                output.writeBytes(2, getMessageBytes());
            }
            if (((bitField0_ & 0x00000004) == 0x00000004)) {
                output.writeInt32(3, chatId_);
            }
            getUnknownFields().writeTo(output);
        }

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeMessageSize(1, user_);
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeBytesSize(2, getMessageBytes());
            }
            if (((bitField0_ & 0x00000004) == 0x00000004)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeInt32Size(3, chatId_);
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        @java.lang.Override
        protected java.lang.Object writeReplace()
                throws java.io.ObjectStreamException {
            return super.writeReplace();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(
                com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /**
         * Protobuf type {@code proto.ChatMessage}
         */
        public static final class Builder extends
                com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                // @@protoc_insertion_point(builder_implements:proto.ChatMessage)
                proto.ChatMessageProto.ChatMessageOrBuilder {
            private int bitField0_;
            private proto.EntitiesProto.User user_ = proto.EntitiesProto.User.getDefaultInstance();
            private com.google.protobuf.SingleFieldBuilder<
                    proto.EntitiesProto.User, proto.EntitiesProto.User.Builder, proto.EntitiesProto.UserOrBuilder> userBuilder_;
            private java.lang.Object message_ = "";
            private int chatId_;

            // Construct using proto.ChatMessageProto.ChatMessage.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(
                    com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
                return proto.ChatMessageProto.internal_static_proto_ChatMessage_descriptor;
            }

            private static Builder create() {
                return new Builder();
            }

            protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
                return proto.ChatMessageProto.internal_static_proto_ChatMessage_fieldAccessorTable
                        .ensureFieldAccessorsInitialized(
                                proto.ChatMessageProto.ChatMessage.class, proto.ChatMessageProto.ChatMessage.Builder.class);
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                    getUserFieldBuilder();
                }
            }

            public Builder clear() {
                super.clear();
                if (userBuilder_ == null) {
                    user_ = proto.EntitiesProto.User.getDefaultInstance();
                } else {
                    userBuilder_.clear();
                }
                bitField0_ = (bitField0_ & ~0x00000001);
                message_ = "";
                bitField0_ = (bitField0_ & ~0x00000002);
                chatId_ = 0;
                bitField0_ = (bitField0_ & ~0x00000004);
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
                return proto.ChatMessageProto.internal_static_proto_ChatMessage_descriptor;
            }

            public proto.ChatMessageProto.ChatMessage getDefaultInstanceForType() {
                return proto.ChatMessageProto.ChatMessage.getDefaultInstance();
            }

            public proto.ChatMessageProto.ChatMessage build() {
                proto.ChatMessageProto.ChatMessage result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            public proto.ChatMessageProto.ChatMessage buildPartial() {
                proto.ChatMessageProto.ChatMessage result = new proto.ChatMessageProto.ChatMessage(this);
                int from_bitField0_ = bitField0_;
                int to_bitField0_ = 0;
                if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                    to_bitField0_ |= 0x00000001;
                }
                if (userBuilder_ == null) {
                    result.user_ = user_;
                } else {
                    result.user_ = userBuilder_.build();
                }
                if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
                    to_bitField0_ |= 0x00000002;
                }
                result.message_ = message_;
                if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
                    to_bitField0_ |= 0x00000004;
                }
                result.chatId_ = chatId_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof proto.ChatMessageProto.ChatMessage) {
                    return mergeFrom((proto.ChatMessageProto.ChatMessage) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(proto.ChatMessageProto.ChatMessage other) {
                if (other == proto.ChatMessageProto.ChatMessage.getDefaultInstance()) return this;
                if (other.hasUser()) {
                    mergeUser(other.getUser());
                }
                if (other.hasMessage()) {
                    bitField0_ |= 0x00000002;
                    message_ = other.message_;
                    onChanged();
                }
                if (other.hasChatId()) {
                    setChatId(other.getChatId());
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public final boolean isInitialized() {
                if (!hasUser()) {

                    return false;
                }
                if (!hasMessage()) {

                    return false;
                }
                if (!hasChatId()) {

                    return false;
                }
                if (!getUser().isInitialized()) {

                    return false;
                }
                return true;
            }

            public Builder mergeFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                    throws java.io.IOException {
                proto.ChatMessageProto.ChatMessage parsedMessage = null;
                try {
                    parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    parsedMessage = (proto.ChatMessageProto.ChatMessage) e.getUnfinishedMessage();
                    throw e;
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            /**
             * <code>required .proto.User user = 1;</code>
             */
            public boolean hasUser() {
                return ((bitField0_ & 0x00000001) == 0x00000001);
            }

            /**
             * <code>required .proto.User user = 1;</code>
             */
            public proto.EntitiesProto.User getUser() {
                if (userBuilder_ == null) {
                    return user_;
                } else {
                    return userBuilder_.getMessage();
                }
            }

            /**
             * <code>required .proto.User user = 1;</code>
             */
            public Builder setUser(
                    proto.EntitiesProto.User.Builder builderForValue) {
                if (userBuilder_ == null) {
                    user_ = builderForValue.build();
                    onChanged();
                } else {
                    userBuilder_.setMessage(builderForValue.build());
                }
                bitField0_ |= 0x00000001;
                return this;
            }

            /**
             * <code>required .proto.User user = 1;</code>
             */
            public Builder setUser(proto.EntitiesProto.User value) {
                if (userBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    user_ = value;
                    onChanged();
                } else {
                    userBuilder_.setMessage(value);
                }
                bitField0_ |= 0x00000001;
                return this;
            }

            /**
             * <code>required .proto.User user = 1;</code>
             */
            public Builder mergeUser(proto.EntitiesProto.User value) {
                if (userBuilder_ == null) {
                    if (((bitField0_ & 0x00000001) == 0x00000001) &&
                            user_ != proto.EntitiesProto.User.getDefaultInstance()) {
                        user_ =
                                proto.EntitiesProto.User.newBuilder(user_).mergeFrom(value).buildPartial();
                    } else {
                        user_ = value;
                    }
                    onChanged();
                } else {
                    userBuilder_.mergeFrom(value);
                }
                bitField0_ |= 0x00000001;
                return this;
            }

            /**
             * <code>required .proto.User user = 1;</code>
             */
            public Builder clearUser() {
                if (userBuilder_ == null) {
                    user_ = proto.EntitiesProto.User.getDefaultInstance();
                    onChanged();
                } else {
                    userBuilder_.clear();
                }
                bitField0_ = (bitField0_ & ~0x00000001);
                return this;
            }

            /**
             * <code>required .proto.User user = 1;</code>
             */
            public proto.EntitiesProto.User.Builder getUserBuilder() {
                bitField0_ |= 0x00000001;
                onChanged();
                return getUserFieldBuilder().getBuilder();
            }

            /**
             * <code>required .proto.User user = 1;</code>
             */
            public proto.EntitiesProto.UserOrBuilder getUserOrBuilder() {
                if (userBuilder_ != null) {
                    return userBuilder_.getMessageOrBuilder();
                } else {
                    return user_;
                }
            }

            /**
             * <code>required .proto.User user = 1;</code>
             */
            private com.google.protobuf.SingleFieldBuilder<
                    proto.EntitiesProto.User, proto.EntitiesProto.User.Builder, proto.EntitiesProto.UserOrBuilder>
            getUserFieldBuilder() {
                if (userBuilder_ == null) {
                    userBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                            proto.EntitiesProto.User, proto.EntitiesProto.User.Builder, proto.EntitiesProto.UserOrBuilder>(
                            getUser(),
                            getParentForChildren(),
                            isClean());
                    user_ = null;
                }
                return userBuilder_;
            }

            /**
             * <code>required string message = 2;</code>
             */
            public boolean hasMessage() {
                return ((bitField0_ & 0x00000002) == 0x00000002);
            }

            /**
             * <code>required string message = 2;</code>
             */
            public java.lang.String getMessage() {
                java.lang.Object ref = message_;
                if (!(ref instanceof java.lang.String)) {
                    com.google.protobuf.ByteString bs =
                            (com.google.protobuf.ByteString) ref;
                    java.lang.String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        message_ = s;
                    }
                    return s;
                } else {
                    return (java.lang.String) ref;
                }
            }

            /**
             * <code>required string message = 2;</code>
             */
            public Builder setMessage(
                    java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000002;
                message_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>required string message = 2;</code>
             */
            public com.google.protobuf.ByteString
            getMessageBytes() {
                java.lang.Object ref = message_;
                if (ref instanceof String) {
                    com.google.protobuf.ByteString b =
                            com.google.protobuf.ByteString.copyFromUtf8(
                                    (java.lang.String) ref);
                    message_ = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            /**
             * <code>required string message = 2;</code>
             */
            public Builder setMessageBytes(
                    com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000002;
                message_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>required string message = 2;</code>
             */
            public Builder clearMessage() {
                bitField0_ = (bitField0_ & ~0x00000002);
                message_ = getDefaultInstance().getMessage();
                onChanged();
                return this;
            }

            /**
             * <code>required int32 chatId = 3;</code>
             */
            public boolean hasChatId() {
                return ((bitField0_ & 0x00000004) == 0x00000004);
            }

            /**
             * <code>required int32 chatId = 3;</code>
             */
            public int getChatId() {
                return chatId_;
            }

            /**
             * <code>required int32 chatId = 3;</code>
             */
            public Builder setChatId(int value) {
                bitField0_ |= 0x00000004;
                chatId_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>required int32 chatId = 3;</code>
             */
            public Builder clearChatId() {
                bitField0_ = (bitField0_ & ~0x00000004);
                chatId_ = 0;
                onChanged();
                return this;
            }

            // @@protoc_insertion_point(builder_scope:proto.ChatMessage)
        }

        // @@protoc_insertion_point(class_scope:proto.ChatMessage)
    }

    // @@protoc_insertion_point(outer_class_scope)
}
